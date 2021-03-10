package com.company;

public class Main {

    public static void main(String[] args) {
        // Карта:
        String[][] card = new String[5][5];
        card[0][0] = "#"; card[0][1] = "s"; card[0][2] = "#"; card[0][3] = "#"; card[0][4] = "#";
        card[1][0] = "."; card[1][1] = "."; card[1][2] = "."; card[1][3] = "."; card[1][4] = "#";
        card[2][0] = "."; card[2][1] = "."; card[2][2] = "."; card[2][3] = "."; card[2][4] = "#";
        card[3][0] = "."; card[3][1] = "#"; card[3][2] = "."; card[3][3] = "."; card[3][4] = "#";
        card[4][0] = "#"; card[4][1] = "#"; card[4][2] = "."; card[4][3] = "f"; card[4][4] = "#";

        byte[] cordBegin = new byte[2]; // Координаты

        // Поиск начала:
        for (byte count1 = 0; count1 < card.length; count1++) {
            for (byte count2 = 0; count2 < card[count1].length; count2++) {
                if (card[count1][count2].equals("s")) {
                    cordBegin[0] = count1;
                    cordBegin[1] = count2;
                }
            }
        }

        byte counter = 0;
        byte[][] cellActiveOld = new byte[card.length * card[0].length][card.length * card[0].length];     // Массив текущих позиций;
        byte[][] cellActiveNew = new byte[card.length * card[0].length][card.length * card[0].length];    // Массив новых позиций;
        byte[][] cellPassability = new byte[card.length * card[0].length][card.length * card[0].length]; // Итоговый Массив проходимых позиций;
        boolean path = false;                                                                           // Определение наличия конца пути;

        cleaner(cellActiveNew);
        cleaner(cellActiveOld);
        cleaner(cellPassability);

        cellActiveOld[0] = cordBegin;                     // Стартовая позиция помещается в массив текущих позиций;
        push(cellPassability, cordBegin[0], cordBegin[1]); // Стартовая позиция заносится в массив пути;

        byte cellNewNum = 1;
        boolean seanseEnd = false;

        // Поиск путей:
        for (byte count = 0, iteration = 1; count < iteration; count++) {

            System.out.println("------------------------------");
            System.out.println("iteration: " + iteration);
            System.out.println("cellNum: " + cellNewNum);

            boolean nullSet = false;
            counter++;

            if (seanseEnd) {
                break;
            }

            for (byte cellIndex = 0, cellNum = cellNewNum; cellIndex < cellNum; cellIndex++) {           // Цикл перебора позиций;

                if (!nullSet) {
                    cellNewNum = 0;
                    nullSet = true;
                }
                byte cellCordY = cellActiveOld[cellIndex][0], cellCordX = cellActiveOld[cellIndex][1]; // Координаты текущей позиции;

                log(cellActiveOld, "now");
                System.out.println("cellY: " + cellCordY + " cellX: " + cellCordX);

                for (byte count1 = -1; count1 < 2; count1++) {
                    for (byte count2 = -1; count2 < 2; count2++) {

                        if (count1 != count2 && count1 != -count2) {

                            byte cordY = (byte) (cellCordY + count1);
                            byte cordX = (byte) (cellCordX + count2);

                            System.out.println("CordY: " + cordY + " cordX: " + cordX);

                            if (cordY >= 0 && cordX >= 0 && cordY < card.length && cordX < card[0].length) {

                                //System.out.println("Отбор значений попадающих на площадь карты;");

                                if (card[cordY][cordX].equals("f")) {
                                    System.out.println("Обнаружен Конец");
                                    seanseEnd = true;
                                    push(cellPassability, cordY, cordX);
                                    break;
                                } else if (card[cordY][cordX].equals(".")) {
                                    //iteration++;
                                    cellNewNum++;
                                    push(cellActiveNew, cordY, cordX);
                                    push(cellPassability, cordY, cordX);
                                    card[cordY][cordX] = String.valueOf(counter);
                                }
                            }
                        }
                    }
                }
            }
            if (cellNewNum == 0) {
                counter--;
            } else {
                iteration++;
            }

            clone(cellActiveNew, cellActiveOld);
            log(cellActiveOld, "res");
            cleaner(cellActiveNew);

            System.out.println("cellNewNum: " + cellNewNum);

            writer(card);

            if (seanseEnd) {
                break;
            }
        }
        // Построение путей:
        log(cellPassability, "pas");
        cellPassability = massiver(cellPassability);                   // Изменяем размеры массива;

        byte[] cellPosition = new byte[2];                           // Для запоминания текущей позиции;
        cellPosition = cellPassability[cellPassability.length - 1]; // Пометка позиций с конца;

        for (byte count1 = (byte) (cellPassability.length - 2); count1 > -1; count1--) {
            byte[][] mas = collector(card, cellPassability, counter);
            boolean inc = true;
            for (byte count2 = 0; count2 < mas.length; count2++) {
                if ((mas[count2][0] == cellPosition[0] && (mas[count2][1] + 1 == cellPosition[1] || mas[count2][1] - 1 == cellPosition[1]))) {
                    if (card[mas[count2][0]][mas[count2][1]].equals(String.valueOf(counter))) {
                        card[mas[count2][0]][mas[count2][1]] = "*";
                        cellPosition = mas[count2];
                        counter--;
                        inc = false;
                        break;
                    }
                } else if ((mas[count2][1] == cellPosition[1] && (mas[count2][0] + 1 == cellPosition[0] || mas[count2][0] - 1 == cellPosition[0]))) {
                    if (card[mas[count2][0]][mas[count2][1]].equals(String.valueOf(counter))) {
                        card[mas[count2][0]][mas[count2][1]] = "*";
                        cellPosition = mas[count2];
                        counter--;
                        inc = false;
                        break;
                    }
                }
            }
            if (inc) {
                counter--;
            }
            log(mas, "col");
        }

        defolter(card);
        writer(card);
    }

    public static void log(byte[][] mas, String str) {
        System.out.print(str + "Mas: ");
        for (byte count = 0; count < mas.length; count++) {
            if (mas[count][0] != -1) {
                System.out.print(mas[count][0] + " " + mas[count][1] + ", ");
            }
        }
        System.out.println();
    }

    public static void push(byte[][] mas, byte y, byte x) {
        for (byte count = 0; count < mas.length; count++) {
            if (mas[count][0] == -1) {
                mas[count][0] = y;
                mas[count][1] = x;
                break;
            }
        }
    }

    public static void clone(byte[][] mas, byte[][] resMas) {
        for (byte count = 0; count < mas.length; count++) {
            if (mas[count][0] != -1) {
                resMas[count][0] = mas[count][0];
                resMas[count][1] = mas[count][1];
            }
        }
    }

    public static void cleaner(byte[][] mas) {
        for (byte count = 0; count < mas.length; count++) {
            mas[count][0] = -1;
            mas[count][1] = -1;
        }
    }

    public static void writer(String[][] card) {
        for (String[] strings : card) {
            StringBuilder str = new StringBuilder();
            for (String string : strings) {
                str.append(string);
            }
            System.out.println(str);
        }
        System.out.println();
    }

    public static void defolter(String[][] card) {
        for (byte count1 = 0; count1 < card.length; count1++) {
            for (byte count2 = 0; count2 < card[count1].length; count2++) {
                if (!card[count1][count2].equals("#") && !card[count1][count2].equals("*") && !card[count1][count2].equals(".") && !card[count1][count2].equals("s") && !card[count1][count2].equals("f")) {
                    card[count1][count2] = ".";
                }
            }
        }
    }

    public static byte[][] massiver(byte[][] mas) {
        byte length = 0;
        byte masLength = (byte) mas.length;

        for (byte count = 0; count < masLength; count++) {
            if (mas[count][0] == -1) {
                break;
            } else {
                length++;
            }
        }

        byte[][] masNew = new byte[length][2];

        for (byte count = 0; count < length; count++) {
            masNew[count][0] = mas[count][0];
            masNew[count][1] = mas[count][1];
        }

        return masNew;
    }

    public static byte[][] collector(String[][] card, byte[][] mas, byte value) {
        byte length = 0;
        byte masLength = (byte) mas.length;
        for (byte count = 0; count < masLength; count++) {
            if (card[mas[count][0]][mas[count][1]].equals(String.valueOf(value))) {
                length++;
            }
        }
        byte[][] masNew = new byte[length][2];
        for (byte count = 0, counter = 0; count < masLength; count++) {
            if (card[mas[count][0]][mas[count][1]].equals(String.valueOf(value))) {
                masNew[counter][0] = mas[count][0];
                masNew[counter][1] = mas[count][1];
                counter++;
                if (counter == length) {
                    break;
                }
            }
        }
        return masNew;
    }
}
