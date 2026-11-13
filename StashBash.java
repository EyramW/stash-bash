import java.util.*;

public class StashBash {

    static final List<String> CATEGORY_NAMES = List.of(
        "Books", "Groceries", "Toiletries", "Clothes",
        "Electronics", "Household", "Beauty", "Other"
    );

    // keyword -> category (this is what the app "knows" and learns)
    static Map<String, String> keywords = new HashMap<>();

    // category -> items on your list
    static Map<String, List<String>> shoppingList = new TreeMap<>();

    static void seed() {
        // Books
        for (String k : List.of("bible", "novel", "journal", "notebook", "textbook", "planner", "devotional", "pen"))
            keywords.put(k, "Books");
        // Groceries
        for (String k : List.of("milk", "eggs", "bread", "rice", "chicken", "bananas", "apples", "cereal", "pasta", "cooking oil"))
            keywords.put(k, "Groceries");
        // Toiletries
        for (String k : List.of("shampoo", "conditioner", "toothpaste", "deodorant", "soap", "body wash", "lotion", "pads", "tampons", "toothbrush"))
            keywords.put(k, "Toiletries");
        // Clothes
        for (String k : List.of("socks", "jeans", "hoodie", "t-shirt", "leggings", "jacket", "bonnet", "underwear"))
            keywords.put(k, "Clothes");
        // Electronics
        for (String k : List.of("charger", "earbuds", "phone case", "usb cable", "power bank", "laptop sleeve", "mouse"))
            keywords.put(k, "Electronics");
        // Household
        for (String k : List.of("paper towels", "trash bags", "detergent", "dish soap", "light bulbs", "sponges", "tissue"))
            keywords.put(k, "Household");
        // Beauty
        for (String k : List.of("mascara", "lip gloss", "foundation", "edge control", "nail polish", "hair oil", "perfume"))
            keywords.put(k, "Beauty");
    }

    // Exact match first, then "contains" match, else Other
    static String suggest(String item) {
        String clean = item.toLowerCase().trim();
        if (keywords.containsKey(clean)) return keywords.get(clean);
        for (Map.Entry<String, String> e : keywords.entrySet()) {
            if (clean.contains(e.getKey())) return e.getValue();
        }
        return "Other";
    }

    static String askForCategory(Scanner in) {
        System.out.println("Pick a category:");
        for (int i = 0; i < CATEGORY_NAMES.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + CATEGORY_NAMES.get(i));
        }
        while (true) {
            System.out.print("Number: ");
            try {
                int n = Integer.parseInt(in.nextLine().trim());
                if (n >= 1 && n <= CATEGORY_NAMES.size()) return CATEGORY_NAMES.get(n - 1);
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a number from the list.");
        }
    }

    static void printList() {
        System.out.println("\n=== YOUR STASH ===");
        if (shoppingList.isEmpty()) {
            System.out.println("(empty)");
            return;
        }
        for (Map.Entry<String, List<String>> e : shoppingList.entrySet()) {
            System.out.println(e.getKey() + ":");
            for (String item : e.getValue()) System.out.println("  - " + item);
        }
    }

    public static void main(String[] args) {
        seed();
        Scanner in = new Scanner(System.in);
        System.out.println("Stash Bash! Type an item, or 'list' to see your stash, or 'quit' to exit.");

        while (true) {
            System.out.print("\nItem: ");
            String item = in.nextLine().trim();
            if (item.isEmpty()) continue;
            if (item.equalsIgnoreCase("quit")) break;
            if (item.equalsIgnoreCase("list")) { printList(); continue; }

            String suggestion = suggest(item);
            System.out.print("Put \"" + item + "\" under " + suggestion + "? (y/n): ");
            String answer = in.nextLine().trim().toLowerCase();

            String chosen = suggestion;
            if (!answer.equals("y")) {
                chosen = askForCategory(in);
                // Learn: remember this correction for next time
                keywords.put(item.toLowerCase(), chosen);
            }

            shoppingList.computeIfAbsent(chosen, k -> new ArrayList<>()).add(item);
            System.out.println("Added to " + chosen + ".");
        }

        printList();
        System.out.println("\nBye!");
    }
}

