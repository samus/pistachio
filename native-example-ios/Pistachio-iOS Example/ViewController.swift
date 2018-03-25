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
        let accountRepo = PistachioInMemoryRepository(name: "accounts")
        ["sam", "bob", "john"].map { Account(name: $0) }.forEach { accountRepo.put(obj: $0) }
        store = PistachioStore(repositories: [accountRepo.name:accountRepo])
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
        store.dispatch(cmd: cmd)
    }
}

class Account: NSObject {
    let name: String

    init(name: String) {
        self.name = name
    }
}

class AccountView: PistachioStoreView {
    var count: Int = 0
    var accounts = [Account]()

    override func initialize(repositories: [String : PistachioRepository]) {
        guard let accountRepo = repositories["accounts"] else { return }
        accounts = accountRepo.scan { _ in return 1 }.flatMap { $0 as? Account }
        count = accounts.count
    }

    override func update(repositories: [String : PistachioRepository], changeSet: PistachioChangeList) {
        guard let accountRepo = repositories["accounts"] else { return }
        accounts = accountRepo.scan { _ in return 1 }.flatMap { $0 as? Account }
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
        changes.added(uuid: accountRepo.put(obj: newAccount))

        return changes
    }
}
