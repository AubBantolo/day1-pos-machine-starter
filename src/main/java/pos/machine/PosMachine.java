package pos.machine;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        Receipt receipt = calculateCost(receiptItems);
        System.out.println(renderReceipt(receipt));
        return renderReceipt(receipt);
    }

    private List<ReceiptItem> decodeToItems(List<String> barcodes) {
        Map<String, Long> itemCountMap = barcodes.stream()
                .collect(Collectors.groupingBy(barcode -> barcode, Collectors.counting()));

        return itemCountMap.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .map(itemCountMapEntry -> buildReceiptItem(itemCountMapEntry))
                .collect(Collectors.toList());
    }

    private ReceiptItem buildReceiptItem(Map.Entry<String, Long> itemCountMapEntry) {
        Item entryItem = ItemsLoader.loadAllItems().stream()
                .filter(item -> item.getBarcode().equals(itemCountMapEntry.getKey()))
                .findFirst()
                .orElse(null);

        return entryItem != null
                ? new ReceiptItem(entryItem.getName()
                , itemCountMapEntry.getValue().intValue()
                , entryItem.getPrice()
                , itemCountMapEntry.getValue().intValue() * entryItem.getPrice())
                : null;
    }

    private Receipt calculateCost(List<ReceiptItem> receiptItems) {
        return new Receipt(receiptItems, calculateTotalPrice(receiptItems));
    }

    private int calculateTotalPrice(List<ReceiptItem> receiptItems) {
        return receiptItems.stream()
                .map(receiptItem -> receiptItem.getSubTotal())
                .reduce((firstSubtotal, secondSubtotal) -> firstSubtotal + secondSubtotal)
                .orElse(0);
    }
    
    private String generateItemsReceipt(Receipt receipt) {
        return receipt.getReceiptItems().stream()
                .map(receiptItem -> "Name: " + receiptItem.getName() + ", Quantity: " + receiptItem.getQuantity() + ", Unit price: " + receiptItem.getUnitPrice() + " (yuan)" + ", Subtotal: " + receiptItem.getSubTotal() + " (yuan)")
                .collect(Collectors.joining("\n"));
    }
}
