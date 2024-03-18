import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/*
Разработать приложение, создающее список плоских геометрических фигур
(круг, квадрат, треугольник и т.д., но не менее 3 фигур).
Создание списка должно быть реализовано несколькими способами
(<ввод с клавиатуры>, чтение из файла, генерирование случайным образом).
Далее необходимо вывести данные о тех фигурах, площадь которых меньше средней площади всех фигур.
При этом вычисление площади фигур может производиться обычным способом, а может округляться по избытку,
по недостатку или до ближайшего значения до указанного пользователем количества знаков после запятой
(вид округления тоже указывается пользователем).
Округление реализовать самостоятельно (через соответствующие методы класса Math),
используя шаблон проектирования «Decorator».
 */
public class Task_02 {

    public static void main(String[] args) {
        new ShapesList();
        ShapesList.getShapes().forEach(IRoundable::calculateArea);
        double allAreas = 0;
        for (IRoundable shape: ShapesList.getShapes()){
            allAreas+= shape.getArea();
        }

        double avrgArea = allAreas/3;
        System.out.println("AVERAGE SHAPE AREA: "+avrgArea);

        for (IRoundable shape: ShapesList.getShapes()){
            if (shape.getArea()<avrgArea){
                System.out.println(shape);
            }
        }
    }
}

interface IRoundable{
    void calculateArea();
    double getArea();
    void setArea(double newArea);
}
class ShapesList{
    static List<IRoundable> shapes = new ArrayList<>();

    public ShapesList() {
        while (shapes.size()<3){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose a shape to add to the list:" +
                    "\nAvailable shapes: 1-square, 2-triangle, 3-circle, 4-exit the program");
            int target = scanner.nextInt();
            switch (target){
                case 1:{
                    new Roundable(new Square());
                    break;
                }
                case 2:{
                    new Roundable(new Triangle());
                    break;
                }
                case 3:{
                    new Roundable(new Circle());
                    break;
                }
                case 4:{
                    System.exit(0);
                }
                default:{
                    System.err.println("INVALID SHAPE. TRY AGAIN");
                    System.out.println();
                }
            }
        }
    }

    public static List<IRoundable> getShapes() {
        return shapes;
    }
}

class Square implements IRoundable{

    private double height;
    private double width;
    private double area;

    @Override
    public void calculateArea() {
        area = width*height;
    }

    public Square() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the height of the square");
        this.height = scanner.nextInt();
        System.out.println("Enter the width of the square");
        this.width = scanner.nextInt();
        ShapesList.shapes.add(this);
    }
    @Override
    public double getArea() {
        return area;
    }

    @Override
    public void setArea(double newArea) {
        area = newArea;
    }

    @Override
    public String toString() {
        return "Square{" +
                "height=" + height +
                ", width=" + width +
                ", area=" + area +
                '}';
    }
}

class Triangle implements IRoundable{
    private double area;
    private double side1;
    private double side2;
    private double side3;
    @Override
    public void calculateArea() {
        double halfP = (side1+side2+side3)/2;
        area = Math.sqrt(halfP*((halfP-side1)*(halfP-side2)*(halfP-side3)));
    }

    public Triangle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter lengths of the triangle sides");
        boolean status = true;
        double[] sides = new double[3];
        while (status){
            System.out.println("Side 1");
            this.side1 = scanner.nextInt();
            sides[0] = side1;
            System.out.println("Side 2");
            this.side2 = scanner.nextInt();
            sides[1] = side2;
            System.out.println("Side 3");
            this.side3 = scanner.nextInt();
            sides[2] = side3;
            Arrays.sort(sides);
            if (sides[0]+sides[1]>sides[2]){
                status = false;
                ShapesList.shapes.add(this);
            } else {
                System.err.println("CANNOT CREATE TRIANGLE WITH GIVEN SIDES. TRY AGAIN");
            }
        }
    }
    @Override
    public double getArea() {
        return area;
    }

    @Override
    public void setArea(double newArea) {
        area = newArea;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "area=" + area +
                ", side1=" + side1 +
                ", side2=" + side2 +
                ", side3=" + side3 +
                '}';
    }
}

class Circle implements IRoundable{

    private double area;
    private double radius;

    @Override
    public void calculateArea() {
        area = 2*Math.PI*radius;
    }

    public Circle() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the radius of the circle");
        this.radius = scanner.nextInt();
        ShapesList.shapes.add(this);
    }
    @Override
    public double getArea() {
        return area;
    }

    @Override
    public void setArea(double newArea) {
        area = newArea;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "area=" + area +
                ", radius=" + radius +
                '}';
    }
}

class Roundable implements IRoundable{
    protected IRoundable shape;
    public Roundable(IRoundable shape) {
        this.shape = shape;
    }

    @Override
    public void calculateArea() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose rounding mode: " +
                "\n1-Do not round" +
                "\n2-Round up to nearest natural number" +
                "\n3-Round down to nearest natural number" +
                "\n4-Round to desirable decimal places");
        int answer = scanner.nextInt();
        shape.calculateArea();
        double area = shape.getArea();
        switch (answer){
            case 1:{
                break;
            }
            case 2:{
                area = Math.ceil(area);
                shape.setArea(area);
            }
            case 3:{
                area = Math.floor(area);
                shape.setArea(area);
            }
            case 4:{
                System.out.println("Number of places to round to: ");
                answer = scanner.nextInt();
                shape.setArea(round(shape.getArea(),answer));
            }
        }
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public double getArea() {
        return 0;
    }

    @Override
    public void setArea(double newArea) {

    }
}
