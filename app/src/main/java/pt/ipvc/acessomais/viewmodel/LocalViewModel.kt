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

    // Estado do utilizador logado
    private val _userEmailLogado = MutableStateFlow<String?>(null)
    val userEmailLogado = _userEmailLogado.asStateFlow()

    // Estado para o local selecionado (foco no mapa)
    private val _localRecemAdicionado = MutableStateFlow<Local?>(null)
    val localRecemAdicionado = _localRecemAdicionado.asStateFlow()

    // Lista de locais filtrada automaticamente pelo email do utilizador
    val locais: StateFlow<List<Local>> = _userEmailLogado
        .flatMapLatest { email ->
            if (email == null) flowOf(emptyList())
            else repository.getLocais(email)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Funções de Autenticação
    fun autenticar(email: String, pass: String, isLogin: Boolean, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (isLogin) {
                val user = dao.getUserByEmail(email)
                if (user != null && user.password == pass) {
                    _userEmailLogado.value = email
                    onSuccess()
                } else {
                    onError("Email ou password incorretos")
                }
            } else {
                val exist = dao.getUserByEmail(email)
                if (exist != null) {
                    onError("O utilizador já existe")
                } else {
                    dao.insertUser(UserEntity(email, pass))
                    _userEmailLogado.value = email
                    onSuccess()
                }
            }
        }
    }

    fun logout() {
        _userEmailLogado.value = null
        _localRecemAdicionado.value = null
    }

    // Funções de Foco (Mapa)
    fun focarLocal(local: Local) {
        _localRecemAdicionado.value = local
    }

    // RESOLVE O ERRO: Unresolved reference 'limparFoco'
    fun limparFoco() {
        _localRecemAdicionado.value = null
    }

    // Funções de Dados
    fun saveLocal(local: Local) {
        viewModelScope.launch {
            repository.saveLocal(local)
            // Opcional: foca automaticamente no local que acabou de ser criado
            _localRecemAdicionado.value = local
        }
    }

    fun deleteLocal(local: Local) {
        viewModelScope.launch {
            repository.deleteLocal(local)
            if (_localRecemAdicionado.value?.id == local.id) {
                limparFoco()
            }
        }
    }
}