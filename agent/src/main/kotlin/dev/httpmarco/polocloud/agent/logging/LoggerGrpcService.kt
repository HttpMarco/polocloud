package dev.httpmarco.polocloud.agent.logging

import dev.httpmarco.polocloud.agent.Agent
import dev.httpmarco.polocloud.v1.logger.LoggerControllerGrpc
import dev.httpmarco.polocloud.v1.logger.LoggerRequest
import dev.httpmarco.polocloud.v1.logger.LoggerResponse
import io.grpc.stub.StreamObserver

class LoggerGrpcService : LoggerControllerGrpc.LoggerControllerImplBase() {

    override fun info(request: LoggerRequest, responseObserver: StreamObserver<LoggerResponse>) {
        val builder = LoggerResponse.newBuilder()

        Agent.logger().info(request.message)
        builder.setSuccess(true)

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

    override fun warn(request: LoggerRequest, responseObserver: StreamObserver<LoggerResponse>) {
        val builder = LoggerResponse.newBuilder()

        Agent.logger().warn(request.message)
        builder.setSuccess(true)

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

    override fun error(request: LoggerRequest, responseObserver: StreamObserver<LoggerResponse>) {
        val builder = LoggerResponse.newBuilder()

        Agent.logger().error(request.message)
        builder.setSuccess(true)

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

    override fun debug(request: LoggerRequest, responseObserver: StreamObserver<LoggerResponse>) {
        val builder = LoggerResponse.newBuilder()

        Agent.logger().debug(request.message)
        builder.setSuccess(true)

        responseObserver.onNext(builder.build())
        responseObserver.onCompleted()
    }

}