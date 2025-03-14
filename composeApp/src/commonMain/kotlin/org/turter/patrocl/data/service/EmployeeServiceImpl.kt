package org.turter.patrocl.data.service

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.turter.patrocl.data.dto.person.EditOwnEmployeePayload
import org.turter.patrocl.data.local.repository.impl.prefs.EmployeeRepositoryImpl
import org.turter.patrocl.data.mapper.person.toEmployeeFromDto
import org.turter.patrocl.data.mapper.person.toEmployeeFromLocal
import org.turter.patrocl.data.mapper.person.toEmployeeLocalFromDto
import org.turter.patrocl.data.remote.client.EmployeeApiClient
import org.turter.patrocl.domain.model.person.Employee
import org.turter.patrocl.domain.service.EmployeeService

class EmployeeServiceImpl(
    private val employeeApiClient: EmployeeApiClient,
    private val employeeRepository: EmployeeRepositoryImpl
//    private val employeeRepository: EmployeeLocalRepository
) : EmployeeService {
    private val log = Logger.withTag("EmployeeRepositoryImpl")

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

//    private val employeeFlow = employeeRepository
//        .get()
//        .map { res -> res.map { it.toEmployeeFromLocal() } }
//        .distinctUntilChanged()

    private val refreshEmployeeFlow = MutableSharedFlow<Unit>(replay = 1)

//    private val employeeDataStatusFlow = MutableStateFlow<BindStatus>(BindStatus.Initial)
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val employeeStateFlow = flow<FetchState<Employee>> {
//        log.d { "Start employee state flow" }
//        //TODO пересмотреть подход и может быть избавиться от refreshEmployeeFlow
//        refreshEmployeeFlow.emit(Unit)
//        refreshEmployeeFlow.collect {
//            log.d { "Collecting employee refresh event" }
//            emit(FetchState.loading())
//            employeeDataStatusFlow.emit(BindStatus.Loading)
//
//            employeeFlow.flatMapLatest { current ->
//                log.d { "Current employee result: $current" }
//                if (current.isSuccess) {
//                    log.d { "Current employee result is success - emit current value" }
//                    flowOf(current)
//                } else {
//                    flow<Result<Employee>> {
//                        log.d { "Current employee result is failure - start updating from remote " }
//                        updateEmployeeFromRemote()
//                        emitAll(employeeFlow)
//                    }
//                }
//            }.collect { result ->
//                result.fold(
//                    onSuccess = { value ->
//                        emit(FetchState.success(value))
//                        log.d { "Employee result is success - emit bind status BIND" }
//                        employeeDataStatusFlow.emit(BindStatus.Bind)
//                    },
//                    onFailure = { cause ->
//                        emit(FetchState.fail(cause))
//                        log.d { "Current employee not present - emit bind status NOT_BIND" }
//                        employeeDataStatusFlow.emit(BindStatus.NotBind)
//                    }
//                )
//            }
//        }
//    }.stateIn(
//        scope = coroutineScope,
//        started = SharingStarted.Lazily,
//        initialValue = FetchState.initial()
//    )

//    override fun getOwnEmployeeStateFlow(): StateFlow<FetchState<Employee>> =
//        employeeStateFlow
//
//    override fun getOwnEmployeeBindStatusStateFlow(): StateFlow<BindStatus> =
//        employeeDataStatusFlow

    override suspend fun checkEmployee() = refreshEmployeeFlow.emit(Unit)

    override suspend fun updateEmployeeFromRemote(): Result<Employee> {
        log.d { "Start updating employee from remote" }
        employeeApiClient.getOwnEmployee().fold(
            onSuccess = { employeeDto ->
                log.d {
                    "Success fetching employee from remote - start replace to local data. " +
                            "EmployeeDto: $employeeDto"
                }
                employeeRepository.setEmployeeId(employeeDto.id)
                employeeRepository.setPreferredCompanyId(employeeDto.preferredCompanyId)
                return Result.success(employeeDto.toEmployeeFromDto())
            },
            onFailure = { cause ->
                log.e { "Fail fetching employee from remote - start cleanup local data" }
                employeeRepository.clear()
                return Result.failure(cause)
            }
        )
    }

    override suspend fun changePreferCompany(preferCompanyId: String): Result<Unit> {
        return employeeApiClient.editOwnEmployee(
            EditOwnEmployeePayload(preferredCompanyId = preferCompanyId)
        )
    }
}