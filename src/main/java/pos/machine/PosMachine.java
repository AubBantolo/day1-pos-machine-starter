package pos.machine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        return null;
    }

    private List<ReceiptItem> decodeToItems(List<String> barcodes) {
        List<Item> items = ItemsLoader.loadAllItems();

        Map<String, Long> itemCountMap = barcodes.stream().collect(Collectors.groupingBy(barcode -> barcode, Collectors.counting()));
        return null;
    }
}
