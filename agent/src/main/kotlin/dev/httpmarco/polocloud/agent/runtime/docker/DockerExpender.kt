package dev.httpmarco.polocloud.agent.runtime.docker

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Frame
import dev.httpmarco.polocloud.agent.runtime.RuntimeExpender

class DockerExpender(val client: DockerClient) : RuntimeExpender<DockerService> {

    override fun executeCommand(
        service: DockerService,
        command: String
    ): Boolean {
        val output = mutableListOf<String>()
        val execId = client.execCreateCmd(service.containerId!!).withAttachStdout(true).withAttachStderr(true).withCmd("sh", "-c", command).exec().id

        client.execStartCmd(execId)
            .exec(object : ResultCallback.Adapter<Frame>() {
                override fun onNext(frame: Frame?) {
                    frame?.payload?.let { output.add(String(it).trim()) }
                }
            }).awaitCompletion()

        val inspect = client.inspectExecCmd(execId).exec()
        return inspect.exitCodeLong == 0L
    }

    override fun readLogs(service: DockerService, lines: Int): List<String> {
        val logLines = mutableListOf<String>()
        client.logContainerCmd(service.containerId!!)
            .withTail(lines)
            .withStdOut(true)
            .withStdErr(true)
            .exec(object : ResultCallback.Adapter<Frame>() {
                override fun onNext(frame: Frame?) {
                    frame?.payload?.let { logLines.add(String(it).trim()) }
                }
            }).awaitCompletion()
        return logLines
    }
}