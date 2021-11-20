package org.line.com.javabase.集合;

import java.util.HashMap;

/**
 * @Author: yangcs
 * @Date: 2021/11/13 8:36
 * @Description:
 */
public class HashMapTest implements Cloneable {
    //----------------------------------------------------------能力有限,全文不涉及红黑树; --------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------属性----------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------------------------------------
    //为什么不直接写出16呢? 这就是在特意提醒你 hashMap 的容量必须是2n次方
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
    // 最大容量
    static final int MAXIMUM_CAPACITY = 1 << 30;
    //负载因子
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //节点 链表转换红黑树的数量  到达8时即刻转换
    static final int TREEIFY_THRESHOLD = 8;
    //节点 红黑树转换链表的数量  到达6时即刻转换 注意: 只有resize 扩容的时候才会转换, 删除则不会转换回链表
    static final int UNTREEIFY_THRESHOLD = 6;
    //最小 转换红黑树最小容量 (注意不是桶内元素数量,而是桶的节点数量)
    static final int MIN_TREEIFY_CAPACITY = 64;
    //初始化是分配的node数组,长度是2次方
    // transient HashMap.Node<K,V>[] table;
    // 见 entrySetTest()
    //其实就是一个迭代器,并无再实现add方法; 直接使用HashMap的table[]数据,两个for循环
    //transient Set<Map.Entry<K,V>> entrySet;
    //桶内元素数量,新增++ ,删除--
    transient int size;
    /**
     * map内元素修改的次数;每次对map修改时(put remove clear meger等),都会自增这个字段;
     * 在各种遍历(情况见下面)时,会校验modCount是否被修改,被修改则抛异常(hashMap并不是多线程安全,一定程度避免并发问题)
     * Iterable的iterator/spliterator方法,Iterable的next方法 会校验;
     * HashIterator->EntrySet(entryIterator) KeySet(keyIterator) Values(valueIterator) 实现Iterable的iterator方法 next方法
     * EntrySet KeySet Values 的forEach
     * hashMap.forEach也会;
     */
    transient int modCount;
    //到达扩容的阈值
    //初始化是  newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    //以后都是  (扩容阀值,不是 newCap*0.75) newThr = oldThr << 1;  (桶内元素数量;其实扩容也不是想象中的*2) newCap = oldCap << 1
    int threshold;
    //负载因子除非构造方法指定,否则都是 DEFAULT_LOAD_FACTOR
    final float loadFactor = DEFAULT_LOAD_FACTOR;


//------------------------------------------------------------------------------------------------------------------------------------------------------------
//---------------------------------------------------------------------内部类----------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * static class Node<K,V>
     *     final K key;
     *     V value;
     *     HashMap.Node<K,V> next;
     *
     * Node类是HashMap实际用于存储的类(key,value);
     * Node类还是一个链表,它拥有 next属性指向下一个Node类;
     * HashMap.Node<K,V>[] table;
     *
     * final class KeySet extends AbstractCollection<V>
     * final class Values extends AbstractCollection<V>
     * final class EntrySet extends AbstractSet<Map.Entry<K,V>>
     * 这三个内部类设计思想都差不多, 都是为hashMap提供遍历的工具; 他们都不存储数据;
     * 他们都实现 <<Iterable>> 接口 foreach 方法 直接使用hashMap table属性 进行双重for循环;
     * KeySet 提供 HashMap的key 遍历
     * Values 提供 HashMap的value 遍历
     * EntrySet 提供 HashMap的node实体 遍历
     *
     * -- Iterable 和 Iterator
     *  Iterable 提供 iterator(),forEach();可以使用foreach遍历,函数式接口入参 提供每一个元素给你;
     *  iterator() 返回Iterator
     *  Iterator 提供 hasNext() next();可以使用iterator迭代器遍历或增强for循环(其实就是iterator的hasNext() next());
     *
     * 都实现了Iterable.iterator()方法,并且提供各自的迭代器
     *
     * KeyIterator
     * ValueIterator
     * EntryIterator
     * 在他们的类中只有一个next() 方法, 分别返回 nextNode().key,nextNode().value和 nextNode()
     * 值得深究的是他们的父类HashIterator和其提供的nextNode()方法 如何实现迭代hashMap;
     *
     * ------ 略有删减, 不影响逻辑
     *    abstract class HashIterator {
     *         HashMap.Node<K,V> next;          // 下一个节点对象
     *         HashMap.Node<K,V> current;       // 当前节点对象
     *         int index;                       // HashMap.table[]的当前下标
     *
     *         HashIterator() {
     *             HashMap.Node<K,V>[] t = table;
     *             current = next = null;
     *             index = 0;
     *             if (t != null && size > 0) { // advance to first entry
     *                 do {} while (index < t.length && (next = t[index++]) == null);
     *             }
     *         }
     *
     *         final HashMap.Node<K,V> nextNode() {
     *             HashMap.Node<K,V>[] t;
     *             HashMap.Node<K,V> e = next;
     *             if (e == null)
     *                 throw new NoSuchElementException();
     *             if ((next = (current = e).next) == null && (t = table) != null) {
     *                 do {} while (index < t.length && (next = t[index++]) == null);
     *             }
     *             return e;
     *         }
     *     }
     *
     *   属性next和current是前后指针,分别指向下一个节点,和当前节点; 每一次获取下一个节点nextNode 时这两个属性都会被重新赋值,以保证对应他们对下一个对象和当前对象的引用
     *   属性 index 是 HashMap.table[]的当前下标;  HashMap的遍历不仅需要遍历整个数组,还需要遍历数组节点上的链表; 当这个节点的蓝标被遍历完, 就需要跳转到下一个节点; index 就是记录当前的节点;
     *
     *   在构造方法中 初始化  current,next和index;
     *   看下这行代码
     *   do {} while (index < t.length && (next = t[index++]) == null);
     *   因为hashMap是无序离散存储的, table[]节点上的链表可以顺序读取,但table[]的第一个节点不一定存在数据,
     *   所以初始化时帮我们定位到了第一个有数据的节点,标注了index; 并且将index位置链表的第一个节点赋值给next属性;
     *   (啥?你说为什么current没有被赋值,而是null? 那你一定没有自己实现过链表)
     *
     *   明白了构造方法逻辑,看nextNode就很简单了;
     *   模板代码: current = next;next = current.next();
     *   如果这个节点的链表遍历完了,寻找下一个不为空为节点;
     *
     *   -----------------------------------------------------------------------------------------------------------
     *   ------------------------------HashMapSpliterator 及其子类提供了并发遍历, 没有研究--------------------------------
     *   -----------------------------------------------------------------------------------------------------------
     */

//------------------------------------------------------------------------------------------------------------------------------------------------------------
//-------------------------------------------------------------------构造方法----------------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * -- 无参构造  loadFactor负载因子使用默认负载因子 DEFAULT_LOAD_FACTOR = 0.75
     *     public HashMap() {
     *         this.loadFactor = DEFAULT_LOAD_FACTOR;
     *     }
     *-- 没啥看头, 调用了其他的构造方法;
     *   public HashMap(int initialCapacity) {
     *         this(initialCapacity, DEFAULT_LOAD_FACTOR);
     *     }
     *-- loadFactor : 负载因子;
     *   initialCapacity : 初始化容器长度; tableSizeFor方法会向上(15->16, 17->32)找到2次方的值并赋予threshold 扩容阀值;
     *   为什么要赋予threshold扩容阀值;它并没有一个字段可以记录当前容器的长度,使用的是table[].length来获取容器长度;而想使用table[]就必须初始化这个数组,
     *   正真的初始化会在第一次put是, 判断table为null,触发resize()方法重新分配table[]和计算threshold 扩容阀值
     *   可能是因为hashMap懒加载思想导致了这个结果
     *   public HashMap(int initialCapacity, float loadFactor) {
     *       this.loadFactor = loadFactor;
     *       this.threshold = tableSizeFor(initialCapacity);
     *   }
     *
     *  -- 入参一个map    默认负载系数与足够的初始容量
     *   public HashMap(Map<? extends K, ? extends V> m) {
     *      this.loadFactor = DEFAULT_LOAD_FACTOR;  // 默认负载系数(0.75)
     *      将老map 元素添加到本map中; 详情见下一节方法解析
     *      putMapEntries(m, false);
     *   }
     *
     * */

//------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------HashMap重要的,我看得懂的方法;-----------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     *--  添加一个map的元素,  HashMap 和putAll 方法使用
     * final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
     *    int s = m.size();
     *  尽量不要扩容,计算最小的阀值; 计算存储这些数据,新容量需要多大容量阀值
     *    float ft = ((float) s / loadFactor) + 1.0F;
     *    如果大于现在的阀值, 需要重新计算阀值
     *    if (ft > threshold)
     *        threshold = tableSizeFor(t);
     *    新增的数据大于阀值 扩容
     *    else if (s > threshold)
     *        resize();
     *    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
     *       putVal(......);
     *    }
     * }
     *
     *-- 计算新的tables数组的长度
     * 通过算法,返回给定目标容量大小的2次方。
     * hashMap table[]要求长度必须是2次方;tableSizeFor 返回值也不许是2次方; (说人话,如果传入的cap不是2次方,则将其提升到2次方的值)
     * 虽然返回值赋予了threshold阀值,但在putval(),第一次真正初始化时threshold也被用于table[]的长度; 转了一圈tableSizeFor()其实就是返回HashMap table[]的长度;
     * static final int tableSizeFor(int cap) {
     *     int n = cap - 1;
     *     n |= n >>> 1;
     *     n |= n >>> 2;
     *     n |= n >>> 4;
     *     n |= n >>> 8;
     *     n |= n >>> 16;
     *     return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
     *  }
     * 努力说一下这个算法(我尽量说明白),
     * 一:如何得到一个2次方?
     * 2次方用2进制来表示的都有一个特点,就是最高位是1,其余位为0; 2->0010; 4->0100; 8->1000; 16->00010000; 32->00100000
     * 我们只需要将所有位都修改为1,最后在加1就可以得到 一个最高位为1,其余位为0的数字,也就是2次方的数字;
     * 二:如何得到一个最邻近的2次方?
     * 2进制还有一个好处,每向上爬升一位,正巧也就是2的次方+1;(2的3次方->2的4次方)
     *
     * 看代码-----
     *  int n = cap - 1;
     *  先减去1,以免n直接符合2次方,导致获取的不是最邻近的2次方(跳跃)
     *  注意: 减去1,并不会影响最高位是1;也不会缩短2进制的长度,除非cap本来就是2次方,(但是不怕,最后+1,长度又会补回来,变回原来的值,也就是原来的2次方);
     *  n |= n >>> 1;
     *  已知最高位是1,这一步就是要最高1位和次高1位 |= 就可以将最高一位和次高一位都修改为1;
     *  n |= n >>> 2;  n |= n >>> 4; n |= n >>> 8; n |= n >>> 16;
     *  最高两位已被处理为1,再向右位移2位并|= 这样就处理了最高4位;依次次类,直到推右位移16位,处理完int的32位;
     *  最后在加1,获得一个最邻近原cap数值的最高位1,其余位为0的数字;
     *
     *
     *--  hashMap hash方法 再次计算hash值
     *    获取key.hashCode()  使用其高16位和低16位进行异或运算;原来的算法没有用到高16位,认为hash不够分散;
     *    使用 &(同1为1,偏向0), |(有1为1,偏向1), ~(对自己取反), 只有^(不同为1,根据概率是正好是公平的)
     *  static final int hash(Object key) {
     *         int h;
     *         return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
     *     }
     *
     *--  hashMap get方法
     *     public V get(Object key) {
     *         Node<K,V> e;
     *         return (e = getNode(hash(key), key)) == null ? null : e.value;
     *     }
     * 调用getNode(hash,key)方法获取节点 e,返回e.value;
     *
     *--  hashMap table节点获取方法
     *     final Node<K,V> getNode(int hash, Object key) {
     *         Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
     *         //初始化tab,first,n
     *         if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
     *              // 总是比较first链表的第一个节点是不是要找的元素
     *             if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
     *                 return first;
     *                 //不是第一个,往下遍历吧
     *             if ((e = first.next) != null) {
     *                 if (first instanceof TreeNode)
     *                     return ((TreeNode<K,V>)first).getTreeNode(hash, key);
     *                 do {
     *                     if (e.hash == hash &&
     *                         ((k = e.key) == key || (key != null && key.equals(k))))
     *                         return e;
     *                 } while ((e = e.next) != null);
     *             }
     *         }
     *         return null;
     *     }
     *
     *  //getNode 重要代码
     *  Node<K,V> first 存储着查找的元素所在的链表;
     *      first = tab[(n - 1) & hash]
     *  重中之重就是计算链表所在table[]的下标; &算法如果不好理解,其实就是%取余算法;目的就是 尽量循环的,离散的分布在table数组上;
     *  前提: n是table[]的长度,也是2次方; hash是经过hash()高低16位异或计算的值;
     *  n-1的2进制必定全部是1;(即: 111111111....)
     *  hash值是离散的,每一位上0/1是随机的,二者&计算(同1为1);因为n-1都是每一位1,所以同位的得到的0/1也是随机的,而其不同的高位则被直接丢弃,避免角标越界;
     *  table[]的长度是 1-n (n是table[]的长度),而通过(n - 1) & hash计算出的数据是0到n-1;与table[]的长度不同,但是与其下标相对应;
     *  至此 tab[(n - 1) & hash] 拿到的就是元素所在的链表;然后判断第一个节点是不是,不是则分别走链表和红黑树的逻辑查询;
     *
     *
     *
     * -- 添加元素
     *   final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
     *         Node<K,V>[] tab; Node<K,V> p; int n, i;
     *         //HashMap还没有初始化,进行初始化
     *         if ((tab = table) == null || (n = tab.length) == 0)
     *             n = (tab = resize()).length;
     *         //元素所在数组下标的链表是空的,开始构建链表节点
     *         if ((p = tab[i = (n - 1) & hash]) == null)
     *             tab[i] = newNode(hash, key, value, null);
     *         else {
     *             Node<K,V> e; K k;
     *             //第一个节点就是 e节点被赋值
     *             if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
     *                 e = p;
     *             //到红黑树里找
     *             else if (p instanceof TreeNode)
     *                 e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
     *             else {
     *             //不是红黑树, 遍历这个链表找;
     *                 for (int binCount = 0; ; ++binCount) {
     *                      //链表都遍历完了, 没有;那就是新增, 构建节点;注意!!!!: e是null,并没有被赋值
     *                     if ((e = p.next) == null) {
     *                         p.next = newNode(hash, key, value, null);
     *                         if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
     *                             treeifyBin(tab, hash);
     *                         break;
     *                     }
     *                     //不小心找到了e,e被赋值;跳出循环;
     *                     if (e.hash == hash &&
     *                         ((k = e.key) == key || (key != null && key.equals(k))))
     *                         break;
     *                     p = e;
     *                 }
     *             }
     *             //上面的代码 其实就是在找节点e,下面的代码就是修改e.value=value
     *            if (e != null) { // existing mapping for key
     *                 V oldValue = e.value;
     *                 if (!onlyIfAbsent || oldValue == null)
     *                     e.value = value;
     *                     //空方法
     *                 afterNodeAccess(e);
     *                 return oldValue;
     *             }
     *
     *         }
     *         //自增修改次数
     *         ++modCount;
     *         //桶内元素两大于阀值,扩容
     *         if (++size > threshold)
     *             resize();
     *         return null;
     *     }
     *
     *   重要代码-----------------------
     *   先看个简单的,但我觉得还是有必要一看
     *   其实就是创建一个节点返回;它可能是链表表头,也可能是其中的节点;
     *     // Create a regular (non-tree) node
     *     Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
     *         return new Node<>(hash, key, value, next);
     *     }
     *    (p = tab[i = (n - 1) & hash])查询元素在table[]的下标,并返回链表;这个在上面分析过了;
     *
     *   hashMap 重点扩容方法来了;-----------------------------

     *   final Node<K,V>[] resize() {
     *         Node<K,V>[] oldTab = table;
     *         int oldCap = (oldTab == null) ? 0 : oldTab.length;
     *         int oldThr = threshold;
     *         int newCap, newThr = 0;
     *         //不是第一次扩容(初始化)
     *         if (oldCap > 0) {
     *             if (oldCap >= MAXIMUM_CAPACITY) {
     *                 threshold = Integer.MAX_VALUE;
     *                 return oldTab;
     *             }
     *             //不是第一次扩容时, 新的table[]长度和新的扩容阀值, 都是右位移一位(*2);扩容阀值不再是容量*负载因子
     *             else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
     *                      oldCap >= DEFAULT_INITIAL_CAPACITY)
     *                 newThr = oldThr << 1; // double threshold
     *         }
     *         // 指定了hashMap table[]的长度;new hashMap(16) 传入的值被向上计算后赋予threshold(懒加载策略), this.threshold = tableSizeFor(initialCapacity) ;
     *           此时 oldThr = threshold;兜兜转转又被赋予了新的table[]长度(newCap容量);
     *         else if (oldThr > 0)
     *             newCap = oldThr;
     *         else {
     *         // 使用默认值 进行初始化
     *             newCap = DEFAULT_INITIAL_CAPACITY;
     *             newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
     *         }
     *
     *         //newThr == 0逻辑,是指定的hashMap长度,在这里处理
     *         由于 threshold原先被借用当做table[]的长度,现在借用结束了,要履行它正在的职责; 重新标记扩容阀值;计算方式 table[]长度乘以负载因子;
     *         if (newThr == 0) {
     *             float ft = (float)newCap * loadFactor;
     *             newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
     *                       (int)ft : Integer.MAX_VALUE);
     *         }
     *
     *         threshold = newThr;
     *         //创建新的数组,并将老数组的值重新映射到新数组上;
     *         Node<K, V>[] newTab = (Node<K,V>[])new Node[newCap];
     *         table = newTab;
     *         if (oldTab != null) {
     *             for (int j = 0; j < oldCap; ++j) {
     *                 Node<K,V> e;
     *                 if ((e = oldTab[j]) != null) {
     *                     oldTab[j] = null;
     *                     //链表只有一个节点,计算e在table[]的下标,将e放在那里;
     *                     if (e.next == null)
     *                         newTab[e.hash & (newCap - 1)] = e;
     *                     else if (e instanceof TreeNode)
     *                         //节点是红黑树,
     *                         ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
     *                     else { // preserve order
     *                         //链表很长,需要遍历;分析见<####1####>
     *                         Node<K,V> loHead = null, loTail = null;
     *                         Node<K,V> hiHead = null, hiTail = null;
     *                         Node<K,V> next;
     *                         do {
     *                             next = e.next;
     *                             if ((e.hash & oldCap) == 0) {
     *                                 if (loTail == null)
     *                                     loHead = e;
     *                                 else
     *                                     loTail.next = e;
     *                                 loTail = e;
     *                             }
     *                             else {
     *                                 if (hiTail == null)
     *                                     hiHead = e;
     *                                 else
     *                                     hiTail.next = e;
     *                                 hiTail = e;
     *                             }
     *                         } while ((e = next) != null);
     *                         if (loTail != null) {
     *                             loTail.next = null;
     *                             newTab[j] = loHead;
     *                         }
     *                         if (hiTail != null) {
     *                             hiTail.next = null;
     *                             newTab[j + oldCap] = hiHead;
     *                         }
     *                     }
     *                 }
     *             }
     *         }
     *         return newTab;
     *     }
     *
     *
     * ####1####
     *  (e.hash & oldCap) == 0
     *  高低位判断
     *  以16table长度为例;
     * 已知原HashMap求table[]的下标使用的 hash()&1111(oldCap-1);
     * hashMap的扩容方式是讲table[]长度<<<1(2次方扩容),求下标的算法也就是 hash()&11111(新oldCap-1);
     * hash&oldCap 也就是hash&10000 也就是判断最高位第五位(也就是新增的那一位)是0吗?
     * 是0,那hash()&11111和hash()&1111得到的都是一样的;也就是说他所在的index是不变得;
     * 不是0,那hash()&11111得到的二进制会比hash()&1111得到的二进制多出一个最高位1,也就是多出二进制10000,也就是原数组长度oldCap;
     * 所以就是当前位置+oldCap(newTab[j + oldCap] = hiHead);
     *
     *  if (loTail == null)
     *      loHead = e;
     *  else
     *      loTail.next = e;
     *  loTail = e;
     *  第一次循环 loHead链表头指针指向第一个元素,loTail当前指针指向第一个元素;
     *  第二次循环 loHead链表头指针不变, loTail当前指针指向的元素的下一个节点设置为循环获取的这个元素;loTail当前指针在链表上前进一步(也就是指向当前循环获取的元素,其实 loTail = loTail.next 更好理解);
     *
     *
     *
     *
     *
     */

//------------------------------------------------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------HashMap 其他简单方法;-----------------------------------------------------------------------------
//------------------------------------------------------------------------------------------------------------------------------------------------------------


    /**
     * --  返回桶内元素;size 新增++,删除--
     * public int size() {
     * return size;
     * }
     * --  判断hashMap 是否为空
     * public boolean isEmpty() {
     * return size == 0;
     * }
     * -- 判断是否存在 某个key
     * public boolean containsKey(Object key) {
     * return getNode(hash(key), key) != null;
     * }
     * --添加元素
     * public V put(K key, V value) {
     * return putVal(hash(key), key, value, false, true);
     * }
     */


}