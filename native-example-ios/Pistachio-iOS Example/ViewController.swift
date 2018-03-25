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
        accountCountLabel.text = "\(accountView.peopleCount)"
    }

    func onViewChanged(view: PistachioStoreView) {

    }
}

class Account: NSObject {
    let name: String

    init(name: String) {
        self.name = name
    }
}

class AccountView: PistachioStoreView {
    var peopleCount: Int = 0
    override func initialize(repositories: [String : PistachioRepository]) {
        guard let accountRepo = repositories["accounts"] else { return }
        self.peopleCount = accountRepo.scan { _ in return 1 }.count
    }
}
