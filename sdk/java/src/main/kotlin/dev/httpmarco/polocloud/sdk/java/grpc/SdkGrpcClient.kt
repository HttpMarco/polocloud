package dev.httpmarco.polocloud.sdk.java.grpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

class SdkGrpcClient {

    var channel: ManagedChannel? = ManagedChannelBuilder.forAddress("127.0.0.1", 8932).usePlaintext().build()


}