import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);
        String command;
         while (true) {
            command = scanner.nextLine().trim();
            if (command.length() == 0) {
                continue;
            }
            if (command.charAt(0) == '/') {
                Status status = Status.parse(command);
                System.out.println(status.getMessage());
                if (status == Status.EXIT) {
                    break;
                }
                continue;
            }
            try {
                calculator.parse(command);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                calculator.clear();
            }

            if (calculator.haveAnswer()) {
                System.out.println(calculator.getAnswer());
                calculator.clear();
            }
        }
    }

/*
    public static int calculator(String[] str) {
        Integer answer = 0;
        Function<Integer, Integer> function = a -> a;
        for (String s : str) {

        }
        return answer;
    }*/
/*
    public static BinaryOperator<Integer> operator(char[] operators) {
        Function<Integer, Integer> function = a -> a;
        BinaryOperator<Integer> binaryOperator = (a, b) -> a;
        for (char operator : operators) {
            switch (operator) {
                case '-':
                    function = function.andThen(subtraction());
                    break;
                case '+':
                    function = function.andThen(addition());
                    break;
                case '*':
                    binaryOperator = (a, b) -> a * b;
                    break;
                case '/':
                    binaryOperator = (a, b) -> a / b;
                    break;
                default:
            }
        }

        binaryOperator.andThen()
        return function.andThen(binaryOperator);
    }

    public static UnaryOperator<Integer> subtraction() {
        return a -> -a;
    }

    public static UnaryOperator<Integer> addition() {
        return a -> a;
    }*/

}
