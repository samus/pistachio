//
//  ViewController.swift
//  Pistachio-iOS Example
//
//  Created by Sam Corder on 3/21/18.
//  Copyright © 2018 Synapptic Labs. All rights reserved.
//

import UIKit
import pistachio

fileprivate let repoName = "Account"

class ViewController: UIViewController, StoreViewListener, UITableViewDataSource {
    
    @IBOutlet var accountCountLabel: UILabel!
    @IBOutlet var newAccountName: UITextField!
    @IBOutlet var accountTable: UITableView!
    
    var store: Store!
    var accountView = AccountView()

    override func viewDidLoad() {
        super.viewDidLoad()
        guard let path = AccountRepository.Companion().pathForRepositoryNamed(name: repoName) else { return }
        print("\(repoName) path is \(path)")
        let accountRepo = AccountRepository(name: repoName, path: path)
        store = Store(repositories: [accountRepo.name: accountRepo], middleware: [LoggingMiddleware()])
        accountView.listener = self
        store.registerView(view: accountView)
        
        accountTable.dataSource = self
    }

    func onViewReady(view: StoreView) {
        refreshUI()
    }

    func onViewChanged(view: StoreView) {
        refreshUI()
    }

    func refreshUI() {
        accountCountLabel.text = "\(accountView.count)"
        accountTable.reloadData()
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return accountView.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "accountCell", for: indexPath)
        let account = accountView.accounts[indexPath.row]
        cell.textLabel?.text = account.name
        return cell
    }
    
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        guard editingStyle == .delete else { return }
        let acct = accountView.accounts[indexPath.row]
        let cmd = DeleteAccountCommand(accountId: acct.id)
        store.dispatch(command: cmd)
    }
    
    @IBAction func addAccount(sender: Any?) {
        guard let name = newAccountName.text, name.isEmpty == false else { return }
        let cmd = AddAccountCommand(accountName: name)
        store.dispatch(command: cmd)
        newAccountName.text = ""
    }
}

class Account: NSObject, NSCoding {
    let id: pistachio.UUID
    let name: String
    
    convenience init(name: String) {
        //This line should not fail with the error:
        //NSInvalidArgumentException', reason: '-[PistachioKeyArchivingRepositoryCompanion create]: unrecognized selector sent to instance 0x600000034180'
        let id = pistachio.UUID.Companion().create()
        self.init(id: id, name: name)
    }
    
    init(id: pistachio.UUID, name: String) {
        self.id = id
        self.name = name
    }

    func encode(with aCoder: NSCoder) {
        aCoder.encode(name, forKey: "name")
        aCoder.encode(id.uuidString, forKey: "id")
    }

    required init?(coder aDecoder: NSCoder) {
        guard let name = aDecoder.decodeObject(forKey: "name") as? String else { return nil }
        self.name = name
        guard let idStr = aDecoder.decodeObject(forKey: "id") as? String,
            let id = pistachio.UUID.Companion().fromString(string: idStr)
            else { return nil }
        self.id = id
    }

}

class AccountRepository: KeyArchivingRepository {
    override func apply(command: Command, changeList: ChangeList) {
        switch command {
        case let addCmd as AddAccountCommand:
            let acct = Account(name: addCmd.accountName)
            self.put(obj: acct, uuid: acct.id)
            changeList.added(repositoryName: self.name, uuid: acct.id)
        case let delCmd as DeleteAccountCommand:
            scan(){ (account) -> KotlinBoolean in
                guard let account = account as? Account else { return false }
                return KotlinBoolean(bool: account.id == delCmd.accountId)
            }
            .compactMap { $0 as? Account }
            .forEach { account in
                self.delete(id: account.id)
                changeList.removed(repositoryName: self.name, uuid: account.id)
            }
        default:
            break
        }
    }
}

class AccountView: StoreView {
    var count: Int = 0
    var accounts = [Account]()

    override func initialize(repositories: [String : Repository]) {
        guard let accountRepo = repositories[repoName] else { return }
        accounts = accountRepo.scan { _ in return true }.compactMap { $0 as? Account }
        count = accounts.count
    }

    override func update(repositories: [String : Repository], changeSet: ChangeList) {
        guard let accountRepo = repositories[repoName] else { return }
        accounts = accountRepo.scan { _ in return true }.compactMap { $0 as? Account }
        count = accounts.count
    }
}

class AddAccountCommand: NSObject, Command {
    let accountName: String

    init(accountName: String) {
        self.accountName = accountName
    }
    
    override var description: String { return "Add Account { accountName = \(accountName) }"}
}

class DeleteAccountCommand: NSObject, Command {
    let accountId: pistachio.UUID
    
    init(accountId: pistachio.UUID) {
        self.accountId = accountId
    }
    
    override var description: String { return "Delete Account { accountId = \(accountId) }"}

}

class LoggingMiddleware: NSObject, Middleware {
    func next(command: Command, repositories: [String : Repository], dispatch: @escaping (Command) -> KotlinUnit) -> Command? {
        print("Executing command: \(command)")
        return command
    }
}
