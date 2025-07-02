package com.fwhyn.lib.baze.common.domain.usecase

import kotlinx.coroutines.flow.Flow

// ----------------------------------------------------------------
/**
 * Abstract class representing a base use case for executing business logic.
 * This class provides a structure for handling asynchronous operations with
 * support for lifecycle notifications, result handling, and timeout management.
 *
 * @param <PARAM> The type of the input parameter required to execute the use case.
 * @param <RESULT> The type of the result produced by the use case.
 */
abstract class FlowUseCaseV2<PARAM, RESULT> : BaseUseCaseV2<PARAM, Flow<RESULT>>()