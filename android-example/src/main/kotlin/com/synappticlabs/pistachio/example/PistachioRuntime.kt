package com.synappticlabs.pistachio.example

import android.content.Context
import com.synappticlabs.pistachio.*
import java.io.File
import java.io.Serializable


val accountRepoName = "Account"

data class Account(val id: UUID = UUIDFactory.create(), val name: String): Serializable

class AccountRepository(context: Context) :
        SerializedRepository<Account>(name = accountRepoName,
                directory = SerializedRepository.directoryForRepositoryNamed(accountRepoName, context),
                context = context) {

    override fun apply(command: Command, changeList: ChangeList) {
        when(command) {
            is AddAccountCommand -> addAccount(command, changeList)
            is DeleteAccountCommand -> deleteAccount(command, changeList)
        }
    }

    private fun addAccount(command: AddAccountCommand, changeList: ChangeList) {
        val account = Account(name = command.accountName)
        put(account, account.id)
        changeList.added(this.name, account.id)
    }

    private fun deleteAccount(command: DeleteAccountCommand, changeList: ChangeList) {
        delete(command.accountId)
        changeList.removed(this.name, command.accountId)
    }
}

class LoggingMiddleware : Middleware {
    override fun next(command: Command, repositories: Map<String, Repository<*>>, dispatch: DispatchFunction): Command? {
        print("Executing command $command")
        return command
    }
}

data class AddAccountCommand(val accountName: String): Command {
    override fun toString(): String {
        return "Add Account { accountName = $accountName }"
    }
}

data class DeleteAccountCommand(val accountId: UUID): Command {
    override fun toString(): String {
        return "Delete Account { accountId = $accountId }"
    }
}

