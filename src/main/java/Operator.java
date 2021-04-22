import java.util.Deque;
import java.util.function.BinaryOperator;

public class Operator {

    private final OperatorInterface operator;

    public Operator(OperatorInterface operator) {
        this.operator = operator;
    }

    public void apply(Deque<Integer> deque) {
        if (isOperation()) {
            deque.push(getOperation().apply(deque.pop(), deque.pop()));
        }
    }

    public boolean isOperation() {
        return this.operator instanceof Operation;
    }

    public boolean isBracket() {
        return this.operator instanceof Brackets;
    }

    public Operation getOperation() {
        return isOperation() ? (Operation) operator : null;
    }

    public Brackets getBracket() {
        return isBracket() ? (Brackets) operator : null;
    }

    public int getPriority() {
        if (isOperation()) {
            return ((Operation) operator).getPriority();
        }
        return 0;
    }

    static Operator parse(String operators) throws OperatorException {
        char operator = operators.charAt(0);
        if (operators.length() > 1) {
            for (int i = 1; i < operators.length(); i++) {
                if (operators.charAt(i) == '-') {
                    if (operator == '+') operator = '-';
                    else if (operator == '-') operator = '+';
                } else {
                    if (operators.charAt(i) != '+') throw new OperatorException(operators);
                }
            }
        }

        for (Operation operatoion : Operation.values()) {
            if (operatoion.getOperator() == operator) return new Operator(operatoion);
        }
        for (Brackets brackets : Brackets.values()) {
            if (brackets.getOperator() == operator) return new Operator(brackets);
        }
        throw new OperatorException(String.valueOf(operator));
    }

    interface OperatorInterface {
    }
    enum Operation implements OperatorInterface {

        ADD((a,b) -> a + b,0,'+'), SUB((a,b) -> b - a,0,'-'), DIV((a,b) -> b / a,1,'/'),
        MUL((a,b) -> a * b,1,'*');

        private final BinaryOperator<Integer> operation;
        private final int priority;
        private final char operator;

        Operation(BinaryOperator<Integer> operation, int priority, char operator) {
            this.operation = operation;
            this.priority = priority;
            this.operator = operator;
        }

        public char getOperator() {
            return operator;
        }

        public int getPriority() {
            return priority;
        }

        public Integer apply(Integer a, Integer b) {
            return this.operation.apply(a,b);
        }
    }

    enum Brackets implements OperatorInterface {
        ROUND_LEFT('(', ')',false), ROUND_RIGHT(')', '(', true);
        private final char operator;
        private final char couple;
        private final boolean right;

        Brackets(char operator, char couple, boolean right) {
            this.operator = operator;
            this.couple = couple;
            this.right = right;
        }

        public boolean isCouple(Brackets bracket) {
            return bracket.getOperator() == this.couple;
        }
        public char getOperator() { return this.operator; }
        public boolean isRight() {
            return this.right;
        }
        public boolean isLeft() {return !this.right; }
    }


}
