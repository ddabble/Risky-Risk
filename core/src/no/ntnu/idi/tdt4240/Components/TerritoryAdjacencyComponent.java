package no.ntnu.idi.tdt4240.Components;

import java.util.HashMap;
import java.util.Map;

public class TerritoryAdjacencyComponent {

    public HashMap<Integer, Integer[]> Borders = new HashMap<Integer, Integer[]>();
    public HashMap<Integer, Integer[]> Continent = new HashMap<Integer, Integer[]>();
    public HashMap<Integer, Territory> TerritoryObjects = new HashMap<Integer, Territory>();
    public HashMap<Integer, Continent> ContinentObjects = new HashMap<Integer, Continent>();

    public void InitializeMap(){
        // North America Territories 1-9
        Borders.put(0x6A8600, new Integer[]{0x3F5437, 0xFFFF00, 0x008040}); // NA1 - As4
        Borders.put(0x3F5437, new Integer[]{0x6A8600, 0xFFFF00, 0x949449, 0xA27300}); // 2
        Borders.put(0xFFFF00, new Integer[]{0x6A8600, 0x3F5437, 0x949449, 0x505027}); // 3
        Borders.put(0x949449, new Integer[]{0x3F5437, 0xFFFF00, 0x505027, 0x808000, 0xD1FF80, 0xA27300}); // 4
        Borders.put(0xD1FF80, new Integer[]{0x949449, 0x808000, 0xA27300}); // 5
        Borders.put(0x505027, new Integer[]{0xFFFF00, 0x949449, 0x808000, 0xFFFF80}); // 6
        Borders.put(0x808000, new Integer[]{0x949449, 0xD1FF80, 0x505027, 0xFFFF80}); // 7
        Borders.put(0xFFFF80, new Integer[]{0x505027, 0x808000, 0xFF8080}); // NA8 - SA1
        Borders.put(0xA27300, new Integer[]{0x3F5437, 0x949449, 0xD1FF80, 0x0000FF}); // NA9 - E1

        // South American Territories 1-4
        Borders.put(0xFF8080, new Integer[]{0x800000, 0x804040, 0xFFFF80});  // SA1 - NA8
        Borders.put(0x800000, new Integer[]{0xFF8080, 0x804040, 0xFF0000}); // 2
        Borders.put(0x804040, new Integer[]{0xFF8080, 0x800000, 0xFF0000, 0xFF915B}); //SA3 - Af1
        Borders.put(0xFF0000, new Integer[]{0x800000, 0x804040}); // 4

        // European Territories 1-7
        Borders.put(0x0000FF, new Integer[]{0x0080FF, 0x004080, 0xA27300}); // E1 - NA9
        Borders.put(0x0080FF, new Integer[]{0x0000FF, 0x004080, 0x000080, 0x000041}); // 2
        Borders.put(0x004080, new Integer[]{0x0000FF, 0x0080FF, 0x000041, 0xF380FF}); // 3
        Borders.put(0x000080, new Integer[]{0x0080FF, 0x000041, 0x43378F, 0x562913, 0x80A480, 0x008000}); // E4 - As1, As5, As10
        Borders.put(0x000041, new Integer[]{0x0080FF, 0x004080, 0xF380FF, 0x43378F}); // 5
        Borders.put(0xF380FF, new Integer[]{0x004080, 0x000041, 0x43378F, 0xFF915B}); // E6 - Af1
        Borders.put(0x43378F, new Integer[]{0x000080, 0x000041, 0xF380FF, 0xFF915B, 0x972900, 0x008000}); // E7 - Af1, Af2, As10


        // African Territories
        Borders.put(0xFF915B, new Integer[]{0x972900, 0xAE5700, 0xFF8000, 0xF380FF, 0x43378F, 0x804040}); // Af1 - E1, E2, SA3
        Borders.put(0x972900, new Integer[]{0xFF915B, 0xFF8000, 0x43378F, 0x008000}); // Af2 - E7, As10
        Borders.put(0xAE5700, new Integer[]{0xFF915B, 0xFF8000, 0x804000}); // 3
        Borders.put(0xFF8000, new Integer[]{0xFF915B, 0x972900, 0xAE5700, 0x804000, 0x6F4B00}); // 4
        Borders.put(0x804000, new Integer[]{0xAE5700, 0xFF8000, 0x6F4B00}); // 5
        Borders.put(0x6F4B00, new Integer[]{0xFF8000, 0x804000}); // 6

        // Asian Territories
        Borders.put(0x562913, new Integer[]{0x006765, 0x80A480, 0x627451, 0x000080}); // As1 - E4
        Borders.put(0x006765, new Integer[]{0x562913, 0x00956D, 0xB3AA00, 0x004000, 0x627451}); // 2
        Borders.put(0x00956D, new Integer[]{0x006765, 0x008040, 0xB3AA00}); // 3
        Borders.put(0x008040, new Integer[]{0x00956D, 0xB3AA00, 0x004000, 0x80FF00, 0x6A8600}); // As4 - NA1
        Borders.put(0x80A480, new Integer[]{0x562913, 0x627451, 0x008000, 0x008080, 0x000080}); // As5 - E4
        Borders.put(0xB3AA00, new Integer[]{0x006765, 0x00956D, 0x008040, 0x004000}); // 6
        Borders.put(0x004000, new Integer[]{0x006765, 0x008040, 0xB3AA00, 0x627451, 0x80FF00}); // 7
        Borders.put(0x627451, new Integer[]{0x562913, 0x006765, 0x80A480, 0x004000, 0x008080, 0x80FF80}); // 8
        Borders.put(0x80FF00, new Integer[]{0x008040, 0x004000}); // 9
        Borders.put(0x008000, new Integer[]{0x80A480, 0x008080, 0x949449, 0x808000, 0x972900, 0xFF8000}); // As10 - E4, E7, A2, A4
        Borders.put(0x008080, new Integer[]{0x80A480, 0x627451, 0x008000, 0x80FF80}); // 11
        Borders.put(0x80FF80, new Integer[]{0x627451, 0x008080, 0x8000FF}); // As12 - Aus1

        // Australian Territories
        Borders.put(0x8000FF, new Integer[]{0xFF00FF, 0x800040, 0x80FF80}); // Aus1 - As12
        Borders.put(0xFF00FF, new Integer[]{0x8000FF, 0x800040, 0x400040}); // 2
        Borders.put(0x800040, new Integer[]{0x8000FF, 0xFF00FF, 0x400040}); // 3
        Borders.put(0x400040, new Integer[]{0xFF00FF, 0x800040}); // 4

        // North America Continent 1
        Continent.put(1, new Integer[]{0x6A8600, 0x3F5437, 0xFFFF00, 0x949449, 0xD1FF80, 0x505027, 0x808000, 0xFFFF80, 0xA27300});

        // South America Continent 2
        Continent.put(2, new Integer[]{0xFF8080, 0x800000, 0x804040, 0xFF0000});

        // Europen Continent 3
        Continent.put(3, new Integer[]{0x0000FF, 0x0080FF, 0x004080, 0x000080, 0x000041, 0xF380FF, 0x43378F});

        // African Continent 4
        Continent.put(4, new Integer[]{0xFF915B, 0x972900, 0xAE5700, 0xFF8000, 0x804000, 0x6F4B00});

        // Asian Continent 5
        Continent.put(5, new Integer[]{0x562913, 0x006765, 0x00956D, 0x008040, 0x80A480, 0xB3AA00, 0x004000, 0x627451, 0x80FF00,
                0x008000, 0x008080, 0x80FF80});

        // Australian Continent 6
        Continent.put(6, new Integer[]{0x8000FF, 0xFF00FF, 0x800040, 0x400040});

    }

    public void InitializeTerritoryObjects(){
        for (Map.Entry<Integer, Integer[]> entry : Borders.entrySet()) {
            TerritoryObjects.put(entry.getKey(), new Territory(entry.getKey()));
        }
    }

    public void InitializeContinentObjects(){
        for (Map.Entry<Integer, Integer[]> entry : Continent.entrySet()) {
            ContinentObjects.put(entry.getKey(), new Continent(entry.getKey(), entry.getValue()));
        }
    }
}
