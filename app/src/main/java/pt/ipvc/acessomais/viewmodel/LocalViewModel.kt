package pt.ipvc.acessomais.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pt.ipvc.acessomais.data.local.AppDatabase
import pt.ipvc.acessomais.data.local.UserEntity
import pt.ipvc.acessomais.data.model.Local
import pt.ipvc.acessomais.data.repo.LocalRepository

@OptIn(ExperimentalCoroutinesApi::class)
class LocalViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getInstance(app).localDao()
    private val repository = LocalRepository(dao)

    private val _userEmailLogado = MutableStateFlow<String?>(null)
    val userEmailLogado = _userEmailLogado.asStateFlow()

    private val _localRecemAdicionado = MutableStateFlow<Local?>(null)
    val localRecemAdicionado = _localRecemAdicionado.asStateFlow()

    private val _mostrarTodos = MutableStateFlow(false)
    val mostrarTodos = _mostrarTodos.asStateFlow()

    val locais: StateFlow<List<Local>> = combine(_userEmailLogado, _mostrarTodos) { email, todos ->
        Pair(email, todos)
    }.flatMapLatest { (email, todos) ->
        if (todos) repository.getAllLocais()
        else if (email == null) flowOf(emptyList())
        else repository.getLocais(email)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun autenticar(email: String, pass: String, isLogin: Boolean, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (isLogin) {
                val user = dao.getUserByEmail(email)
                if (user != null && user.password == pass) {
                    _userEmailLogado.value = email
                    onSuccess()
                } else onError("Email ou password incorretos")
            } else {
                val exist = dao.getUserByEmail(email)
                if (exist != null) onError("O utilizador já existe")
                else {
                    dao.insertUser(UserEntity(email, pass))
                    _userEmailLogado.value = email
                    onSuccess()
                }
            }
        }
    }

    fun toggleMostrarTodos() { _mostrarTodos.value = !_mostrarTodos.value }

    fun logout() {
        _userEmailLogado.value = null
        _localRecemAdicionado.value = null
    }

    fun focarLocal(local: Local) { _localRecemAdicionado.value = local }

    fun limparFoco() { _localRecemAdicionado.value = null }

    fun saveLocal(local: Local) {
        viewModelScope.launch {
            repository.saveLocal(local)
            _localRecemAdicionado.value = local
        }
    }

    fun deleteLocal(local: Local) {
        viewModelScope.launch {
            repository.deleteLocal(local)
            if (_localRecemAdicionado.value?.id == local.id) limparFoco()
        }
    }
}