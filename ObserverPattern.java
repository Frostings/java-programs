import java.util.HashSet;
import java.util.Iterator;


class Subject {
    protected HashSet <Observer> observers = new HashSet <Observer>();

    
    void attach(Observer ob) {
        observers.add(ob);
    }
    
    
    void detach(Observer ob) {
        observers.remove(ob);
    }
    
    
    void notifyObservers() {
        Iterator<Observer> iterator = observers.iterator();
        while(iterator.hasNext()) {
            iterator.next().onNotify(this);
        }
    }
}


abstract class Observer {
    abstract void onNotify(Subject subject);
}


class Item extends Subject {
    String name;
    private boolean onSale = false;
    
    
    Item(String name) {
        this.name = name;
    }
    
    
    void setOnSale(boolean value) {
        onSale = value;
        if (onSale) {
            notifyObservers();
        }
    }
}


class Customer extends Observer {
    String name;
    
    Customer(String name) {
        this.name = name;
    }
    
    void onNotify(Subject subject) {
        System.out.println(((Item)subject).name + " is on sale, " + name);
    }
}


abstract class SubjectAndObserver {
    protected HashSet <SubjectAndObserver> observers = new HashSet <SubjectAndObserver>();

    
    void attach(SubjectAndObserver ob) {
        observers.add(ob);
    }
    
    
    void detach(SubjectAndObserver ob) {
        observers.remove(ob);
    }
    
    
    void notifyObservers() {
        Iterator<SubjectAndObserver> iterator = observers.iterator();
        while(iterator.hasNext()) {
            iterator.next().onNotify(this);
        }
    }
    
    abstract void onNotify(SubjectAndObserver subject);
}

class Profile extends SubjectAndObserver {
    String name;
    
	
    Profile(String name) {
        this.name = name;
    }
    
    
    void onNotify(SubjectAndObserver subject) {
        System.out.println(((Profile)subject).name + " updated their status, " + name);
    }
    
	
    void updateStatus() {
        notifyObservers();
    }
}


public class ObserverPattern {
    public static void main(String []args) {
        Item tshirt = new Item("tshirt");
        Customer bill = new Customer("Bill");
        Customer jill = new Customer("Jill");
        
        tshirt.attach(bill);
        tshirt.attach(jill);
        tshirt.setOnSale(true);
        
        Profile alice = new Profile("Alice");
        Profile bob = new Profile("Bob");
        bob.attach(alice);
        alice.attach(bob);
        bob.updateStatus();
        alice.updateStatus();
    }
}