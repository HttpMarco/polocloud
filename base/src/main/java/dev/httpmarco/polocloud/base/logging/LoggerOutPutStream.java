package dev.httpmarco.polocloud.base.logging;

import dev.httpmarco.polocloud.api.CloudAPI;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor
@AllArgsConstructor
public final class LoggerOutPutStream extends ByteArrayOutputStream {

    private boolean errorStream = false;

    @Override
    public void flush() throws IOException {
        super.flush();

        var input = this.toString(StandardCharsets.UTF_8);
        super.reset();

        if (input != null && !input.isEmpty()) {
            input = input.replace("\n", "");
            if (!errorStream) {
                CloudAPI.instance().logger().info(input);
            } else {
                CloudAPI.instance().logger().error(input, null);
            }
        }
    }
}
