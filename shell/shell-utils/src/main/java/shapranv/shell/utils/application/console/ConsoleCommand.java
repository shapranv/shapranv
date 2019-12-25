package shapranv.shell.utils.application.console;

enum ConsoleCommand {
    HELP("-h", "Help"),
    SERVICES("-s", "Service registry"),
    EXIT("-q", "Stop application"),
    UNDEF("", "");

    private String code;
    private String description;

    ConsoleCommand(String code, String description) {
        this.code = code;
        this.description = description;
    }

    String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    static ConsoleCommand findByCode(String code) {
        for (ConsoleCommand command : values()) {
            if (command.code.equals(code)) {
                return command;
            }
        }

        return UNDEF;
    }
}
