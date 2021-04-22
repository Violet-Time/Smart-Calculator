enum Status {
    EXIT("Bye!"), HELP("The program calculates the sum of numbers"), UNKNOWN("Unknown command");

    private String message;

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public static Status parse(String command) {
        return switch (command) {
            case "/exit" -> EXIT;
            case "/help" -> HELP;
            default -> UNKNOWN;
        };
    }
}