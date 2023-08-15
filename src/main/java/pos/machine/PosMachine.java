package pos.machine;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        return null;
    }

    private List<ReceiptItem> decodeToItems(List<String> barcodes) {
        Map<String, Long> itemCountMap = barcodes.stream()
                .collect(Collectors.groupingBy(barcode -> barcode, Collectors.counting()));

        return itemCountMap.entrySet()
                .stream()
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
}
