package shapranv.shell.utils.application.console;

public enum ConsoleCommand {
    HELP("-h", "Help"),
    SERVICES("-s", "Service registry"),
    EXIT("-q", "Stop application"),
    STATUS("-status", "Print status"),
    START_SERVICE("-start", "Start service"),
    STOP_SERVICE("-stop", "Stop service"),
    UNDEF("", "");

    private String code;
    private String description;

    ConsoleCommand(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ConsoleCommand findByCode(String code) {
        for (ConsoleCommand command : values()) {
            if (command.code.equals(code)) {
                return command;
            }
        }

        return UNDEF;
    }
}
