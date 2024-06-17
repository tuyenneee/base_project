package com.ivnd.knowledgebase.facade;

import com.fasterxml.jackson.databind.JsonNode;
import com.ivnd.knowledgebase.common.BaseFacade;
import com.ivnd.knowledgebase.anotation.Facade;
import com.ivnd.knowledgebase.model.entity.Province;
import com.ivnd.knowledgebase.service.ProvinceService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.function.*;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 02/05/2024
 */
@Facade
public class ProvinceFacade extends BaseFacade<ProvinceService> {
    private final RestTemplate restTemplate;
    private final String url;
    public ProvinceFacade(ProvinceService service, RestTemplate restTemplate,
                          @Value("${address.baseUrl}") String url) {
        super(service);
        this.restTemplate = restTemplate;
        this.url = url;
    }

    public JsonNode getProvince() {
        return restTemplate.getForEntity(url, JsonNode.class).getBody();
    }

    public void create() {
        service.save(new Province());
    }
    @NoArgsConstructor
    public static class Consumer {
        public void biConsumer(BiConsumer<String, String> biConsumer) {
            biConsumer.accept("a", " Duy Nến kìaaaa");
        }

        public List<String> biFunction(BiFunction<String, String, List<String>> biFunction) {
            return biFunction.apply("Tuyển", "a");
        }
    }

    public static void main(String[] args) {
//        String a = "0.0";
//        String c = "0.1999";
//        var doo = 0.231231233123213217E23;
//        System.out.println(doo);
//        var d = Double.parseDouble(c);
//        var b = Double.parseDouble(a);
//        var s = String.format("%.2f", b);
//        var sa = String.format("%.2f", doo);
//        System.out.println(sa.substring(0,sa.lastIndexOf("0")));
//        System.out.println(s);
//        System.out.println(sa);
//        Predicate<String> bool = m -> m.equals("Tuyennee");
//        List<String> names = new ArrayList<>();
//        names.add("djasldjsa");
//        names.add("Tuyennee");
//        names.add("dsadas321321");
//        names.add("djasldasdasdjsa");
//        System.out.println(names.stream().anyMatch(name -> name.equals("Tuyennee")));
//        System.out.println(names.contains("Tuyennee"));
//        BiConsumer<String, String> bi = (a, b) -> {
//            if (a.equals("b")) {
//                System.out.println(a.concat(b));
//            } else {
//                System.out.println("Chúng ta nợ Duy Nến 1 lời xin lỗi! :(");
//            }
//        };
//        Consumer a = new Consumer();
//        a.biConsumer(bi);
//        List<String> b = new ArrayList<>();
//        List<String> c = new ArrayList<>();
//        BiFunction<String, String, List<String>> function = (s, v) -> {
//            b.add(s);
//            b.add(v);
//            return b;
//        };
//
//        BiFunction<String, String, List<String>> function2 = (s, v) -> {
//            c.add(s);
//            c.add(v);
//            return c;
//        };
//        Supplier<String> supplier = () -> a.biFunction(function2).stream()
//                .collect(Collectors.joining(","));
//        var str = supplier.get();
//        System.out.println(str);
//        var list = Arrays.asList(str.split(","));
//        System.out.println("new list = " + list);
//        System.out.println(a.biFunction(function));
    }

    @Getter
    @Setter
    private Node head;

    public void removeMiddleNodes() {
        if (head == null || head.next == null) {
            return; // Empty list or single node, nothing to remove
        }

        Node slow = head;
        Node fast = head;
        Node prev = null;

        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }

        // If size is even, remove two middle nodes
        if (fast != null) {
            prev.next = slow.next;
        } else {
            // Size is odd, remove only the middle node
            prev.next = slow.next;
        }
    }

    // ... other methods for adding/removing nodes (optional)

    private static class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
        }
    }
}
