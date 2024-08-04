allprojects {
    dependencies {
        compileOnly(project(":api"))
        compileOnly(project(":launcher"))
        compileOnly(project(":node"))
    }
}