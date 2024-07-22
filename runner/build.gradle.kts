/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

dependencies {

}

tasks.jar {
    // add instance loader for services
    from(project(":common").tasks.jar)
    from(project(":base").tasks.jar)
    from(project(":api").tasks.jar)
    from(project(":instance").tasks.jar)
    from(project(":plugin").tasks.jar)

    manifest {
        attributes["Premain-Class"] = "dev.httpmarco.polocloud.runner.RunnerBootstrap"
        attributes["Main-Class"] = "dev.httpmarco.polocloud.runner.RunnerBootstrap"
    }
    archiveFileName.set("polocloud.jar")
}