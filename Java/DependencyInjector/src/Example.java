
@InjectEligible(clazz = ExampleTwo.class)
public class Example {

    private ExampleTwo exampleTwo;

    public Example() {
    }

    public void a(){
        exampleTwo.provideService();
    }

    public int b() {
        exampleTwo.provideService();
        return 0;
    }

    public int z(int a, int b, int c) {
        exampleTwo.provideService();
        return 0;
    }
}
