//
//  ViewController.swift
//  Pistachio-iOS Example
//
//  Created by Sam Corder on 3/21/18.
//  Copyright © 2018 Synapptic Labs. All rights reserved.
//

import UIKit
import Pistachio

class ViewController: UIViewController, PistachioStoreViewListener {
    @IBOutlet var accountCountLabel: UILabel!
    @IBOutlet var newAccountName: UITextField!

    var store: PistachioStore!
    var accountView = AccountView()

    override func viewDidLoad() {
        super.viewDidLoad()
        guard let accountRepo = PistachioKeyArchivingRepositoryCompanion().repositoryNamed(name: "accounts") else { return }
        store = PistachioStore(repositories: [accountRepo.name: accountRepo], middleware: [LoggingMiddleware()])
        accountView.listener = self
        store.registerView(view: accountView)
    }

    func onViewReady(view: PistachioStoreView) {
        refreshUI()
    }

    func onViewChanged(view: PistachioStoreView) {
        refreshUI()
    }

    func refreshUI() {
        accountCountLabel.text = "\(accountView.count)"
    }

    @IBAction func addAccount(sender: Any?) {
        guard let name = newAccountName.text, name.isEmpty == false else { return }
        let cmd = AddAccountCommand(accountName: name)
        store.dispatch(command: cmd)
    }
}

class Account: NSObject, NSCoding {

    init(name: String) {
        self.name = name
    }

    func encode(with aCoder: NSCoder) {
        aCoder.encode(name, forKey: "name")
    }

    required init?(coder aDecoder: NSCoder) {
        guard let n = aDecoder.decodeObject(forKey: "name") as? String else { return nil }
        self.name = n
    }

    let name: String
}

class AccountView: PistachioStoreView {
    var count: Int = 0
    var accounts = [Account]()

    override func initialize(repositories: [String : PistachioRepository]) {
        guard let accountRepo = repositories["accounts"] else { return }
        accounts = accountRepo.scan { _ in return 1 }.compactMap { $0 as? Account }
        count = accounts.count
    }

    override func update(repositories: [String : PistachioRepository], changeSet: PistachioChangeList) {
        guard let accountRepo = repositories["accounts"] else { return }
        accounts = accountRepo.scan { _ in return 1 }.compactMap { $0 as? Account }
        count = accounts.count
    }
}

class AddAccountCommand: NSObject, PistachioCommand {
    let accountName: String

    init(accountName: String) {
        self.accountName = accountName
    }

    func apply(repositories: [String : PistachioRepository]) -> PistachioChangeList {
        let changes = PistachioChangeList()
        guard let accountRepo = repositories["accounts"] else { return changes }
        let newAccount = Account(name: accountName)
        changes.added(repositoryName: accountRepo.name, uuid: accountRepo.put(obj: newAccount))
        return changes
    }
}

class LoggingMiddleware: NSObject, PistachioMiddleware {
    func next(command: PistachioCommand,
              repositories: [String : PistachioRepository],
              dispatch: @escaping (PistachioCommand) -> PistachioStdlibUnit) -> PistachioCommand? {
        print("Executing command: \(command)")
        return command
    }
}
