import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private final Deque<Operator> operators;
    private final Deque<Integer> numbers;
    private final Map<String, Integer> variables;
    private Operator tmpOperation;
    //Pattern pattern = Pattern.compile("([-+]?\\d+)|([+\\-*/]+)|([()])|(([a-zA-Z]+) *= *(\\d+))");
    private final Pattern pattern = Pattern.compile("(?<appr>([a-zA-Z]*) *(.*)= *([+-]?\\d*)([a-zA-Z]*)(.*))|" +
                                                    "(?<var>((([^a-zA-Z (])?([a-zA-Z]+))|(([^\\d (+-])?([+-]?\\d+)))([^ )])?)|" +
                                                    "(?<operator>([()])|(([^ +\\-]*)([+\\-]+)([^ +\\-]*))|(([^ ]*)([*/])([^ ]*)))");

    Calculator() {
        this.operators = new LinkedList<>();
        this.numbers = new LinkedList<>();
        this.variables = new HashMap<>();
    }

    public Integer getAnswer() {
        return numbers.getFirst();
    }

    public boolean haveAnswer() {
        return numbers.size() == 1;
    }

    public void apply(Operator operator) throws OperatorException {
        while (!operators.isEmpty() &&
                operators.getFirst().isOperation() &&
                operator.getPriority() <= operators.getFirst().getPriority()) {
            operators.pop().apply(numbers);
        }
    }

    public void apply() throws OperatorException {
        while (!operators.isEmpty() &&
                operators.getFirst().isOperation()) {
            operators.pop().apply(numbers);
        }
    }

    public void clear() {
        this.operators.clear();
        this.numbers.clear();
    }

    public boolean isFind(String str) {
        return str != null && !str.isEmpty();
    }

    private void parseAssign(Matcher matcher) throws Exception {
        if (isFind(matcher.group(3)) || isFind(matcher.group(6))) {
            throw new Exception("Invalid expression: The name of variable should contain only Latin letters.");
        }
        if (isFind(matcher.group(4))) {
            if (isFind(matcher.group(5))) {
                throw new Exception("Invalid expression: The value can be an integer number or a value of another variable.");
            }
            variables.put(matcher.group(2), Integer.parseInt(matcher.group(4)));
        }
        if (isFind(matcher.group(5))) {
            if (!variables.containsKey(matcher.group(5))) {
                throw new Exception("Invalid expression: The value can be an integer number or a value of another variable.");
            }
            variables.put(matcher.group(2), variables.get(matcher.group(5)));
        }
    }

    private void parseVariable(Matcher matcher) throws Exception {
        if (isFind(matcher.group(9))) {
            if (isFind(matcher.group(10)) || isFind(matcher.group(15))) {
                throw new Exception("Invalid expression: ");
            }
            if (!variables.containsKey(matcher.group(11))) {
                throw new Exception("unknown");
            }
            numbers.push(variables.get(matcher.group(11)));
        }
        if (isFind(matcher.group(12))) {
            if (isFind(matcher.group(13)) || isFind(matcher.group(15))) {
                throw new Exception("Invalid expression: ");
            }
            numbers.push(Integer.parseInt(matcher.group(14)));
        }
    }

    private void parseOperator(Matcher matcher) throws Exception {
        if (isFind(matcher.group(17))) {
            tmpOperation = Operator.parse(matcher.group(17));
            if (tmpOperation.getBracket().isRight()) {
                apply();
                if (!operators.isEmpty() &&
                        operators.getFirst().isBracket() &&
                        operators.getFirst().getBracket().isCouple(tmpOperation.getBracket())) {
                    operators.pop();
                } else {
                    throw new Exception("Invalid expression: Brackets");
                }
            } else {
                operators.push(tmpOperation);
            }
        }
        if (isFind(matcher.group(18))) {
            if (isFind(matcher.group(19)) || isFind(matcher.group(21))) {
                throw new Exception("Invalid expression: ");
            }
            tmpOperation = Operator.parse(matcher.group(20));
            apply(tmpOperation);
            operators.push(tmpOperation);
        }
        if (isFind(matcher.group(22))) {
            if (isFind(matcher.group(23)) || isFind(matcher.group(25))) {
                throw new Exception("Invalid expression: ");
            }
            tmpOperation = Operator.parse(matcher.group(24));
            apply(tmpOperation);
            operators.push(tmpOperation);
        }
    }

    public void parse(String str) throws Exception{
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            if (isFind(matcher.group("appr"))) {
                parseAssign(matcher);
            } else if (isFind(matcher.group("var"))) {
                parseVariable(matcher);
            } else if (isFind(matcher.group("operator"))) {
                parseOperator(matcher);
            }
        }
        apply();
        if (!operators.isEmpty() || numbers.size() > 1) {
            throw new Exception("Invalid expression");
        }
    }
}
