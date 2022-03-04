import java.util.ArrayList;
import java.util.Scanner;


class Eye extends Trait {
    public static class Colour extends Property {
        enum Value { Brown, Black, Blue, Green; }

        public Colour() {
            super(Value.values());
        }
    }
    
    public static class Size extends Property {
        enum Value { Big, Medium, Small; }

        public Size() {
            super(Value.values());
        }
    }

    public Eye() {
        properties.add(new Colour());
        properties.add(new Size());
    }
}

class Gender extends Trait {
    public static class GenderProperty extends Property {
        enum Value { Male, Female; }

        public GenderProperty() {
            super(Value.values());
        }
    }

    public Gender() {
        properties.add(new GenderProperty());
    }
}

class Hair extends Trait {
    public static class Colour extends Property {
        enum Value { Brown, Black, Red, Blond; }

        public Colour() {
            super(Value.values());
        }
    }
    
    public static class Length extends Property {
        enum Value { ShortHair, MediumHair, LongHair; }

        public Length() {
            super(Value.values());
        }
    }

    public Hair() {
        properties.add(new Colour());
        properties.add(new Length());
    }
}

class Name extends Trait {
    public static class FirstName extends Property {
        enum Value { Alice, Bob, Cassandra, Dylan, Alan, Leonhard; }

        public FirstName() {
            super(Value.values());
        }
    }

    public static class LastName extends Property {
        enum Value { Smith, Anderson, King, Turing, Euler }

        public LastName() {
            super(Value.values());
        }
    }

    public Name() {
        properties.add(new FirstName());
        properties.add(new LastName());
    }
}

class Trait {
    ArrayList<Property> properties;

    public Trait() {
        properties = new ArrayList<Property>();
    }

    public String toString() {
        return ("" + getClass()).substring(6);
    }
}


class Property {
    Enum value;
    Enum[] values;

    public Property(Enum[] values) {
        this.values = values;
        value = values[(int)(Math.random() * values.length)];
    }

    public String toString() {
        return ("" + getClass()).substring(("" + getClass()).indexOf("$") + 1);
    }
}


class Person {
    ArrayList<Trait> traits;
    boolean faceUp = true;

    public Person() {
        traits = new ArrayList<Trait>();
        traits.add(new Name());
        traits.add(new Gender());
        traits.add(new Hair());
        traits.add(new Eye());
    }

    public void display() {
        for(Trait t: traits) {
            String trait = ("" + t.getClass()).substring(6);
            for(Property p: t.properties) {
                System.out.println(trait + " " + p + ": " + p.value);
            }
        }
        System.out.println();
    }
}


public class GuessWho {
    Person[] persons = new Person[25];
    Person target;
    Scanner scan = new Scanner(System.in);
    int numFaceUp = 25;

    public GuessWho() {
        for(int i = 0; i < 25; i++) {
            persons[i] = new Person();
        }
        target = persons[(int)(Math.random() * 25)];
    }

    public void guess() {
        System.out.println("Guess about: ");
        for (int i = 0; i < target.traits.size(); i++) {
            System.out.println(i + ": " + target.traits.get(i));
        }

        int traitIndex = scan.nextInt();
        ArrayList<Property> p = target.traits.get(traitIndex).properties;
        for (int j = 0; j < p.size(); j++) {
            System.out.println(j + ": " + p.get(j));
        }

        int propertyIndex = scan.nextInt();
        for (int j = 0; j < p.get(propertyIndex).values.length; j++) {
            System.out.println(j + ": " + p.get(propertyIndex).values[j]);
        }

        int valueIndex = scan.nextInt();
        int targetValue = target.traits.get(traitIndex).properties.get(propertyIndex).value.ordinal();
        boolean correct = targetValue == valueIndex;
        for (Person person: persons) {
            if (!person.faceUp) {
                continue;
            }
            int personValue = person.traits.get(traitIndex).properties.get(propertyIndex).value.ordinal();
            if (correct && personValue != valueIndex) {
                person.faceUp = false;
                numFaceUp--;
            } else if (!correct && personValue == valueIndex) {
                person.faceUp = false;
                numFaceUp--;
            }
        }
    }

    public void display() {
        for(Person p: persons) {
            if (!p.faceUp) {
                continue;
            }
            p.display();
        }
    }
    public void start() {
        int numGuesses = 0;
        while(numFaceUp > 1) {
            display();
            guess();
            numGuesses++;
        }
        System.out.println("The person was " + target.traits.get(0).properties.get(0).value + " " + target.traits.get(0).properties.get(1).value);
        System.out.println("NUM GUESSES: " + numGuesses);
    }
  
    public static void main(String[] args) {
        GuessWho game = new GuessWho();
        game.start();
    }
}
