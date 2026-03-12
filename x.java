import java.util.*;

/* ================= USER CLASS ================= */

class User{

    String id;
    String name;
    String campus;
    String role;

    User(String id,String name,String campus,String role){
        this.id=id;
        this.name=name;
        this.campus=campus;
        this.role=role;
    }
}

/* ================= MENU ITEM ================= */

class MenuItem{

    int id;
    String name;
    int price;

    MenuItem(int id,String name,int price){
        this.id=id;
        this.name=name;
        this.price=price;
    }
}

/* ================= ORDER CLASS ================= */

class Order{

    int orderId;
    String user;
    String item;
    int qty;
    int total;
    boolean vip;

    Order(int orderId,String user,String item,int qty,int total,boolean vip){
        this.orderId=orderId;
        this.user=user;
        this.item=item;
        this.qty=qty;
        this.total=total;
        this.vip=vip;
    }
}

/* ================= LINKED LIST ================= */

class OrderNode{

    Order data;
    OrderNode next;

    OrderNode(Order data){
        this.data=data;
        this.next=null;
    }
}

class OrderLinkedList{

    OrderNode head=null;

    void addOrder(Order order){

        OrderNode newNode=new OrderNode(order);

        if(head==null){
            head=newNode;
            return;
        }

        OrderNode temp=head;

        while(temp.next!=null)
            temp=temp.next;

        temp.next=newNode;
    }

    void display(){

        OrderNode temp=head;

        while(temp!=null){

            System.out.println(temp.data.orderId+" "+temp.data.item+" Rs"+temp.data.total);

            temp=temp.next;
        }
    }
}

/* ================= MAIN SYSTEM ================= */

public class x{

    static Scanner sc=new Scanner(System.in);

    static ArrayList<MenuItem> menu=new ArrayList<>();

    static HashMap<String,User> users=new HashMap<>();

    static Queue<Order> orderQueue=new LinkedList<>();

    static PriorityQueue<Order> vipOrders =
            new PriorityQueue<>((a,b)->b.total-a.total);

    static Stack<Order> history=new Stack<>();

    static OrderLinkedList orderList=new OrderLinkedList();


/* ================= CAMPUS DETECTION ================= */

static String detectCampus(String regId){

    if(regId.contains("200"))
        return "Bachupally";

    if(regId.contains("000"))
        return "Vijayawada";

    if(regId.contains("100"))
        return "Aziznagar";

    return "Unknown";
}

/* ================= LOGIN ================= */

static User login(){

    System.out.println("\n--- LOGIN ---");

    System.out.println("1 Student");
    System.out.println("2 Faculty");
    System.out.println("3 Admin");
    System.out.println("4 Guest");

    int role=sc.nextInt();

    if(role==1){

        System.out.print("Enter Registration ID: ");
        String id=sc.next();

        if(users.containsKey(id)){

            User u=users.get(id);

            System.out.println("Login Successful");
            System.out.println("Welcome "+u.name);
            System.out.println("Campus: "+u.campus);

            return u;
        }

        System.out.println("Invalid Student");
        return null;
    }

    if(role==2){

        System.out.print("Enter Faculty ID: ");
        String id=sc.next();

        if(users.containsKey(id)){

            System.out.println("Faculty Login Successful");

            return users.get(id);
        }

        return null;
    }

    if(role==3){

        System.out.print("Enter Admin Password: ");

        String pass=sc.next();

        if(pass.equals("Admin@123")){

            System.out.println("Admin Login Successful");

            return new User("admin","Admin","KL","Admin");
        }

        return null;
    }

    if(role==4){

        System.out.println("Guest Login");

        int otp=(int)(Math.random()*9000)+1000;

        System.out.println("Generated OTP: "+otp);

        System.out.print("Enter OTP: ");

        int userOtp=sc.nextInt();

        if(userOtp==otp){

            System.out.println("OTP Verified");

            return new User("guest","Guest","Guest","Guest");
        }

        System.out.println("Invalid OTP");

        return null;
    }

    return null;
}

/* ================= MENU ================= */

static void addMenu(int id,String name,int price){

    menu.add(new MenuItem(id,name,price));
}

static void showMenu(){

    System.out.println("\n--- MENU ---");

    for(MenuItem m:menu){

        System.out.println(m.id+" "+m.name+" Rs"+m.price);
    }
}

/* ================= MERGE SORT ================= */

static void mergeSort(int left,int right){

    if(left>=right)
        return;

    int mid=(left+right)/2;

    mergeSort(left,mid);
    mergeSort(mid+1,right);

    merge(left,mid,right);
}

static void merge(int left,int mid,int right){

    ArrayList<MenuItem> temp=new ArrayList<>();

    int i=left;
    int j=mid+1;

    while(i<=mid && j<=right){

        if(menu.get(i).id <= menu.get(j).id)
            temp.add(menu.get(i++));
        else
            temp.add(menu.get(j++));
    }

    while(i<=mid)
        temp.add(menu.get(i++));

    while(j<=right)
        temp.add(menu.get(j++));

    for(int k=0;k<temp.size();k++)
        menu.set(left+k,temp.get(k));
}

/* ================= BINARY SEARCH ================= */

static MenuItem binarySearch(int id){

    int left=0;
    int right=menu.size()-1;

    while(left<=right){

        int mid=(left+right)/2;

        if(menu.get(mid).id==id)
            return menu.get(mid);

        if(menu.get(mid).id<id)
            left=mid+1;
        else
            right=mid-1;
    }

    return null;
}

/* ================= PLACE ORDER ================= */

static void placeOrder(String user){

    System.out.print("Menu ID: ");
    int id=sc.nextInt();

    MenuItem item=binarySearch(id);

    if(item==null){

        System.out.println("Item not found");
        return;
    }

    System.out.print("Quantity: ");
    int qty=sc.nextInt();

    System.out.print("VIP order? (t/f): ");

    String v=sc.next();

    boolean vip=v.equalsIgnoreCase("t");

    int total=item.price*qty;

    Order order=new Order(
            new Random().nextInt(10000),
            user,
            item.name,
            qty,
            total,
            vip
    );

    orderList.addOrder(order);

    if(vip)
        vipOrders.add(order);

    else
        orderQueue.add(order);

    int waitTime=orderQueue.size()*5;

    System.out.println("Order placed for "+item.name);

    System.out.println("Estimated waiting time: "+waitTime+" minutes");
}

/* ================= PROCESS ORDER ================= */

static void processOrder(){

    Order order=null;

    if(!vipOrders.isEmpty())
        order=vipOrders.poll();

    else if(!orderQueue.isEmpty())
        order=orderQueue.poll();

    else{

        System.out.println("No orders");
        return;
    }

    history.push(order);

    System.out.println("Processing order "+order.item);
}

/* ================= TRANSACTIONS ================= */

static void showHistory(){

    System.out.println("\n--- TRANSACTIONS ---");

    for(Order o:history){

        System.out.println(o.orderId+" "+o.item+" Rs"+o.total);
    }
}

/* ================= ADMIN PANEL ================= */

static void adminPanel(){

    while(true){

        System.out.println("\n--- ADMIN PANEL ---");

        System.out.println("1 Add Menu Item");
        System.out.println("2 Remove Menu Item");
        System.out.println("3 View Menu");
        System.out.println("4 View Orders");
        System.out.println("5 Process Order");
        System.out.println("6 Exit Admin");

        int ch=sc.nextInt();

        switch(ch){

            case 1:

                System.out.print("Item ID: ");
                int id=sc.nextInt();

                System.out.print("Item Name: ");
                String name=sc.next();

                System.out.print("Price: ");
                int price=sc.nextInt();

                addMenu(id,name,price);

                System.out.println("Item Added");

                break;

            case 2:

                System.out.print("Enter Item ID: ");

                int rid=sc.nextInt();

                menu.removeIf(m -> m.id==rid);

                System.out.println("Item Removed");

                break;

            case 3:

                showMenu();

                break;

            case 4:

                orderList.display();

                break;

            case 5:

                processOrder();

                break;

            case 6:

                return;
        }
    }
}

/* ================= MAIN ================= */

public static void main(String[] args){

    users.put("2520030271",
            new User("2520030271","Raghava",detectCampus("2520030271"),"Student"));

    users.put("FAC001",
            new User("FAC001","Faculty","KL","Faculty"));

    addMenu(1,"Veg Thali",60);
    addMenu(2,"Chicken Biryani",120);
    addMenu(3,"Coffee",25);
    addMenu(4,"Sandwich",40);

    mergeSort(0,menu.size()-1);

    User currentUser=login();

    if(currentUser==null){

        System.out.println("Login Failed");
        return;
    }

    if(currentUser.role.equals("Admin")){

        adminPanel();
        return;
    }

    while(true){

        System.out.println("\n--- KLCM SYSTEM ---");

        System.out.println("1 View Menu");
        System.out.println("2 Place Order");
        System.out.println("3 Process Order");
        System.out.println("4 Show Orders");
        System.out.println("5 Show Transactions");
        System.out.println("6 Exit");

        int ch=sc.nextInt();

        switch(ch){

            case 1:
                showMenu();
                break;

            case 2:
                placeOrder(currentUser.name);
                break;

            case 3:
                processOrder();
                break;

            case 4:
                orderList.display();
                break;

            case 5:
                showHistory();
                break;

            case 6:
                System.exit(0);
        }
    }
}
}