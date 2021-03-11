import java.util.*;

@FunctionalInterface
interface ArraySupplier<T> {
    T[] get(int length);
}

class BinaryHeap<T>{
        T[] heap;
        int size;
        Comparator<T> comparator;
        
        public BinaryHeap(ArraySupplier<T> supplier,int max_size, Comparator<T> comparator) {
            this.heap = supplier.get(max_size);
            this.size = 0;
            this.comparator = comparator;
        }
 
        boolean empty(){
            return size == 0;
        }
 
 
        void insert(T t){
            int i = size++;
            heap[i] = t;
            decreaseweight(i, t);
        }
       
       
        void decreaseweight(int i, T t) {
            heap[i] = t;
            while(i > 0){
                int parent = (i + 1)/2 - 1;
                
                if(comparator.compare(heap[parent],heap[i]) <= 0) break;
                T temp = heap[i];   
                heap[i] = heap[parent];
                heap[parent] = temp;
                i = parent;
            }
        }
 
        T extractMin(){
            T min = heap[0];
            heap[0] = heap[--size];
            heapify(0);
            return min;
        }
 
        void heapify(int i){
            int l = 2*i + 1;
            int r = l + 1;
            int smallest = i;
            if(l <= size && comparator.compare(heap[l],heap[smallest]) < 0){
                    smallest = l;
            }
            if(r <= size && comparator.compare(heap[r],heap[smallest]) < 0){
                    smallest = r;
            }
            if(smallest != i){
                    T temp = heap[smallest];
                    heap[smallest] = heap[i];
                    heap[i] = temp;
                    heapify(smallest);
            }
        }
       
     
}