import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*
Разработать приложение, генерирующее несколько последовательностей чисел (по заранее заданному алгоритму,
например, последовательность чисел Фибоначчи,
последовательность простых чисел,
последовательность факториалов целых неотрицательных чисел).
Генерирование типа последовательности и количество генерируемых элементов должно определяться пользователем.
Для каждой последовательности после генерации указать время работы соответствующего алгоритма.
Определение этого времени реализовать, используя шаблон проектирования «Decorator».
 */
public class Task_01 {
    public static void main(String[] args) {
        IExecutable iExecutable = new ExecTimeCalculator(new Sequence());
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose sequence to calculate:" +
                "\nTo choose - enter the sequence type in any case" +
                "\nFIB - Fibonacci sequence" +
                "\nPRIME - prime numbers sequence" +
                "\nFACTORIAL - sequence of the factorials of numbers");
        String type = scanner.nextLine();
        type = type.toUpperCase();
        boolean state = false;
        while (!state){
            switch (type){
                case "FIB":{
                    Sequence.setType(Type.FIB);
                    state = true;
                    break;
                }
                case "PRIME":{
                    Sequence.setType(Type.PRIME);
                    state = true;
                    break;
                }
                case "FACTORIAL":{
                    Sequence.setType(Type.FACTORIAL);
                    state = true;
                    break;
                }
                default:{
                    System.err.println("INVALID SEQUENCE TYPE" +
                            "\nAVAILABLE OPTIONS: " +
                            "FIB, PRIME, FACTORIAL");
                    type = scanner.nextLine();
                    type = type.toUpperCase();
                }
            }
        }
        iExecutable.execute();
    }
}

enum Type {FIB, PRIME, FACTORIAL}

interface IExecutable{
    void execute();
}
class Sequence implements IExecutable{
    private static Type type;
    private void fibs_sequence(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many numbers do you want in this sequence?");
        int target = scanner.nextInt();
        BigInteger n1 = BigInteger.ZERO;
        BigInteger n2 = BigInteger.ONE;
        switch (target){
            case 0: break;
            case 1: {
                System.out.println(n1);
                break;
            }
            case 2: {
                System.out.println(n1);
                System.out.println(n2);
                break;
            }
            default:{
                System.out.println(n1);
                System.out.println(n2);
                BigInteger n3;
                for (int i = 0; i < target-2; i++) {
                    n3 = n1.add(n2);
                    n1 = n2;
                    n2 = n3;
                    System.out.println(n3);
                }
            }
        }
    }
    private void primes_sequence(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many numbers do you want in this sequence?");
        int target = scanner.nextInt();
        switch (target){
            case 0: break;
            case 1:{
                System.out.println(2);
            }
            break;
            case 2:{
                System.out.println(2);
                System.out.println(3);
            }
            break;
            default:{
                findPrimes(target);
            }
        }
    }
    private void factorials(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many numbers do you want in this sequence?");
        int target = scanner.nextInt();
        if (target!=0){
            int count = 1;
            BigInteger res = BigInteger.ONE;
            while (count<=target){
                res = res.multiply(BigInteger.valueOf(count));
                count++;
                System.out.println(res);
            }
        }
    }

    private void findPrimes(int target){
        List<Integer> primes = new ArrayList<>();
        primes.add(2);
        primes.add(3);
        int n = 3;
        while (primes.size()<target){
            boolean isPrime = true;
            for (Integer num: primes){
                if (n%num==0){
                    isPrime = false;
                    break;
                }
            }
            if (isPrime){
                primes.add(n);
            }
            n+=2;
        }
        primes.forEach(System.out::println);
    }
    public void execute(){
        switch (type){
            case FIB: fibs_sequence();
            break;
            case PRIME: primes_sequence();
            break;
            case FACTORIAL: factorials();
        }
    }

    public static void setType(Type type) {
        Sequence.type = type;
    }
}

class ExecTimeCalculator implements IExecutable{
    protected IExecutable iExecutable;

    public ExecTimeCalculator(IExecutable iExecutable) {
        this.iExecutable = iExecutable;
    }

    @Override
    public void execute() {
        long start = System.nanoTime();
        iExecutable.execute();
        long end = System.nanoTime()-start;
        if (end>1_000_000){
            double endToMilliS = (double) end /1_000_000;
            System.out.printf("EXECUTION TIME: %.4f MILLISECONDS",endToMilliS);
        } else {
            System.out.printf("EXECUTION TIME: %d NANOSECONDS",end);
        }
    }
}
