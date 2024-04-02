import java.io.FileWriter;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Создаю список строк с данными об игрушках
        String[] toyStrings = {
                "1 Конструктор 2",
                "2 Робот 2",
                "3 Кукла 6"
        };

        // Создаю объект ToyStoreConstructor и передаю ему строки с данными об игрушках
        ToyStoreConstructor toyStoreConstructor = new ToyStoreConstructor(toyStrings);

        // Получаю данные об игрушках из объекта ToyStoreConstructor
        List<Integer> ids = toyStoreConstructor.getIds();
        List<String> names = toyStoreConstructor.getNames();
        List<Integer> frequencies = toyStoreConstructor.getFrequencies();

        // Создаю объект магазина игрушек и добавляю игрушки
        ToyStoreGame toyStore = new ToyStoreGame(ids, names, frequencies);

        // Вызываю метод getToy() 10 раз и записываю результат в файл
        for (int i = 0; i < 10; i++) {
            Toys toy = toyStore.getToy();
            toyStore.writeToLogFile(toy);
            System.out.println("Игрушка: " + toy.getName());
        }
    }
}

class ToyStoreConstructor {
    private List<Integer> ids = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<Integer> frequencies = new ArrayList<>();

    public ToyStoreConstructor(String[] toyStrings) {
        for (String toyString : toyStrings) {
            String[] parts = toyString.split(" ");
            if (parts.length == 3) {
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int frequency = Integer.parseInt(parts[2]);
                ids.add(id);
                names.add(name);
                frequencies.add(frequency);
            } else {
                System.out.println("Некорректный формат строки: " + toyString);
            }
        }
    }

    public List<Integer> getIds() {
        return ids;
    }

    public List<String> getNames() {
        return names;
    }

    public List<Integer> getFrequencies() {
        return frequencies;
    }
}

class Toys {
    private int id;
    private String name;
    private int frequency;

    public Toys(int id, String name, int frequency) {
        this.id = id;
        this.name = name;
        this.frequency = frequency;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public String toString() {
        return "Toy{" + "id=" + id + ", name='" + name + '\'' + ", frequency=" + frequency + "}";
    }
}

class ToyStoreGame {
    private final Queue<Toys> toysQueue = new PriorityQueue<>((t1, t2) -> t2.getFrequency() - t1.getFrequency());
    private final List<Integer> ids;
    private final List<String> names;
    private final List<Integer> frequencies;

    public ToyStoreGame(List<Integer> ids, List<String> names, List<Integer> frequencies) {
        this.ids = ids;
        this.names = names;
        this.frequencies = frequencies;
        putToys();
    }

    public void putToys() {
        for (int i = 0; i < ids.size(); i++) {
            toysQueue.add(new Toys(ids.get(i), names.get(i), frequencies.get(i)));
        }
    }

    public Toys getToy() {
        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1; // Генерирую случайное число от 1 до 10

        int threshold = 0;

        for (Toys toy : toysQueue) {
            threshold += toy.getFrequency();
            if (randomNumber <= threshold) {
                return toy;
            }
        }
        return null;
    }

    public void writeToLogFile(Toys toy) {
        try (FileWriter writer = new FileWriter("toys.txt", true)) {
            if (toy != null) {
                writer.write(toy.toString() + "\n");
            } else {
                writer.write("ToyStore is empty\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



