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
    implementation(project(":api"))
    implementation(project(":instance"))
    implementation(libs.osgan.netty)
    /*
    api(libs.minimessage)
    implementation(libs.minimessage)
    implementation(libs.bungeeminimessage)
    // TODO:
     */

    implementation(libs.spigot)
    implementation(libs.bungeecord)

    implementation(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks.jar {
    archiveFileName.set("plugin.jar")
}