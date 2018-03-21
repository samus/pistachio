//
//  ViewController.swift
//  Pistachio-iOS Example
//
//  Created by Sam Corder on 3/21/18.
//  Copyright © 2018 Synapptic Labs. All rights reserved.
//

import UIKit
import Pistachio

class ViewController: UIViewController {
    var store: PistachioStore!

    override func viewDidLoad() {
        super.viewDidLoad()
        let accountRepo = PistachioInMemoryRepository(name: "accounts")
        ["sam", "bob", "john"].map { Account(name: $0) }.forEach { accountRepo.put(obj: $0) }
        store = PistachioStore(repositories: [accountRepo])
    }
}

class Account: NSObject {
    let name: String

    init(name: String) {
        self.name = name
    }
}
