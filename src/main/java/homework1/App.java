package homework1;

public class App {

    public static void main(String[] args) {

        Box<Apple> appleBox = new Box<>();
        Box<Apple> newAppleBox = new Box<>();
        Box<Orange> orangeBox = new Box<>();
        Box<Orange> newOrangeBox = new Box<>();

        for (int i = 0; i < 5; i++) {
            appleBox.addFruit(new Apple());
            orangeBox.addFruit(new Orange());
        }


        System.out.println("Weight of box with apples: " + appleBox.getWeight());
        System.out.println("Weight of box with oranges: " + orangeBox.getWeight());
        System.out.println();

        System.out.println("Compare weight of boxes with apples and apples: " + appleBox.compareBox(appleBox));
        System.out.println("Compare weight of boxes with apples and oranges: " + appleBox.compareBox(orangeBox));
        System.out.println();

        System.out.println("Apples from box are being poured to new box...");
        appleBox.changeBox(newAppleBox);

        System.out.println("Weight of box with apples: " + appleBox.getWeight());
        System.out.println("Weight of new box with apples: " + newAppleBox.getWeight());
        System.out.println();

        System.out.println("Oranges from box are being poured to new box...");
        orangeBox.changeBox(newOrangeBox);
        System.out.println("Weight of box with oranges: " + orangeBox.getWeight());
        System.out.println("Weight of new box with oranges: " + newOrangeBox.getWeight());
    }
}
