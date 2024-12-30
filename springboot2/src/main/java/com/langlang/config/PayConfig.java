package com.langlang.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {


    /** 以前开发的时候, 遇到过yaml 配置了  payId: 000092204142622
     * 以 0 开头的纯数字字符串, 最后配置文件解析填充到配置类里的值是一个其它数字的字符串, 简直是莫名其妙, 此次有时间可以重现问题深入理解解析的原理.
     * @Value 注解和Autowired一样 都是 AutowiredAnnotationBeanPostProcessor#postProcessProperties 解析赋值.
     * 在创建 payConfig 这个bean, 使用构造方法创建bean 后, 在 populateBean 方法里:
     * for (BeanPostProcessor bp : getBeanPostProcessors()) {
     * 				if (bp instanceof InstantiationAwareBeanPostProcessor) {
     * 					InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
     * 					PropertyValues pvsToUse = ibp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName);
     *  类型强转后, 调用postProcessProperties 方法. metadata.inject() 方法处理注入
     *  一路debug 直到  PropertySourcesPropertyResolver #getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) { // 该方法被调用2次, 这是第二次
     *    for (PropertySource<?> propertySource : this.propertySources) {  //this.propertySources.getClass() 是这个 MutablePropertySources 对象
     *              // 集合里7 个对象, 最后获取的逻辑在 OriginTrackedMapPropertySource.getProperty(key)
     * 				Object value = propertySource.getProperty(key);
     *
     *  this.propertySources 属性声明
     *  private final PropertySources propertySources;
     *  这是一个接口 恰好只有一个实现类  public interface PropertySources extends Iterable<PropertySource<?>> {
     *  实现类:   MutablePropertySources implements PropertySources {
     *  private final List<PropertySource<?>> propertySourceList = new CopyOnWriteArrayList<>();
     *  用一个list集合保存一组 PropertySource 对象. 这种设计在spring 中使用还是挺多的
     *
     *  public Object getProperty(String name) {
     * 		Object value = super.getProperty(name); //  调用父类 MapPropertySource.getProperty(key)
     * 	...
     * 	MapPropertySource 方法
     * 	public Object getProperty(String name) {
     * 		// this.source 正好有三个值, 数据类型 java.util.Collections$UnmodifiableMap 使用idea 调试可以看到值是 172826
     * 	    // 说明这个值解析的时候已经转换过了
     * 		return this.source.get(name);
     * 	}
     *
     *  既然 this.source.get(name) 获取到的值已经是错误的, 说明问题可能解析保存的时候
     *  把断点先打在 OriginTrackedMapPropertySource 构造方法的第一行 # this(name, source, false); #	super(name, source);
     *  debug 启动后, 断点停在了 super(name, source);
     *  方法调用栈往前翻看, 	Collections.unmodifiableMap(loaded.get(i)) 是 source 的参数.
     *  查看 loaded 里面有一个hashMap 存储着配置的3个参数 再往前可以找到 loaded 赋值的地方
     *  	List<Map<String, Object>> loaded = new OriginTrackedYamlLoader(resource).load();
     *  接下来再次 debug 进入 load 方法
     *  loadAll 方法
     *   Iterator<Object> result = new Iterator<Object>() {
     *             @Override
     *             public boolean hasNext() {
     *                 return constructor.checkData();
     *             }
     *             @Override
     *             public Object next() {
     *                 return constructor.getData();
     *             }
     *             @Override
     *             public void remove() {
     *                 throw new UnsupportedOperationException();
     *             }
     *         };
     *      constructor.getData(); 进入这个方法 具体处理的过程, 不断的递归调出处理配置在yaml 里的 key, value
     *   org.yaml.snakeyaml.constructor.BaseConstructor#getData()  里面有核心两大步骤:
     *
     *    Node node = composer.getNode(); // 递归解析 yaml, 给每个配置项的值打标签, 最后组装成一个 node
     *    return constructDocument(node); //
     *
     *   org.yaml.snakeyaml.constructor.BaseConstructor#constructMapping2ndStep(org.yaml.snakeyaml.nodes.MappingNode, java.util.Map)
     *   for (NodeTuple tuple : nodeValue) {
     *             Node keyNode = tuple.getKeyNode();
     *             Node valueNode = tuple.getValueNode();
     *             Object key = constructObject(keyNode);
     *             Object value = constructObject(valueNode);
     *             mapping.put(key, value);
     *   } // 循环结束, map 里面保存了解析的键值对, 最后一直返回组装成一个map, 把map 转成list 集合,
     *   最后遍历中 Collections.unmodifiableMap(loaded.get(i)) 处理成不可修改的 map
     *   一步步走 进入下面这个方法
     *   org.yaml.snakeyaml.constructor.BaseConstructor#constructObjectNoCheck(Node)
     *      Construct constructor = getConstructor(node);  //
     *      Object data = (constructedObjects.containsKey(node)) ? constructedObjects.get(node)
     *                 : constructor.construct(node);  三目运算符结果false, 走后面的逻辑
     *
     *   org.yaml.snakeyaml.constructor.SafeConstructor.ConstructYamlInt#construct(org.yaml.snakeyaml.nodes.Node)
     *   这个方法转 int 类型
     *     @Override
     *         public Object construct(Node node) {
     *             String value = constructScalar((ScalarNode) node).toString().replaceAll("_", "");
     *             int sign = +1;
     *             char first = value.charAt(0);
     *             if (first == '-') {
     *                 sign = -1;
     *                 value = value.substring(1);
     *             } else if (first == '+') {
     *                 value = value.substring(1);
     *             }
     *             int base = 10;
     *             if ("0".equals(value)) {
     *                 return Integer.valueOf(0);
     *             } else if (value.startsWith("0b")) {
     *                 value = value.substring(2);
     *                 base = 2;
     *             } else if (value.startsWith("0x")) {
     *                 value = value.substring(2);
     *                 base = 16;
     *             } else if (value.startsWith("0")) {
     *                 value = value.substring(1);   // 0 开头 走这个逻辑
     *                 base = 8;
     *             } else if (value.indexOf(':') != -1) {
     *                 String[] digits = value.split(":");
     *                 int bes = 1;
     *                 int val = 0;
     *                 for (int i = 0, j = digits.length; i < j; i++) {
     *                     val += Long.parseLong(digits[j - i - 1]) * bes;
     *                     bes *= 60;
     *                 }
     *                 return createNumber(sign, String.valueOf(val), 10);
     *             } else {
     *                 return createNumber(sign, value, 10);
     *             }
     *             return createNumber(sign, value, base);
     *  org.yaml.snakeyaml.constructor.SafeConstructor#createNumber(int, java.lang.String, int)
     *  核心处理  把 number 当作8 进制的数转成10 进制的
     *     result = Integer.valueOf(number, radix); // number="0000521432", radix=8
     *     result = 172826
     *     后续把这个转后的值进行封装, 处理存储在map 中
     *  前面为什么进入 SafeConstructor.ConstructYamlInt#construct(org.yaml.snakeyaml.nodes.Node)
     *  再调试一次看看 getConstructor(node)
     *    protected Construct getConstructor(Node node) {
     *         if (node.useClassConstructor()) {
     *             return yamlClassConstructors.get(node.getNodeId());
     *         } else {
     *             Construct constructor = yamlConstructors.get(node.getTag());
     *             if (constructor == null) {
     *                 for (String prefix : yamlMultiConstructors.keySet()) {
     *                     if (node.getTag().startsWith(prefix)) {
     *                         return yamlMultiConstructors.get(prefix);
     *                     }
     *                 }
     *                 return yamlConstructors.get(null);
     *             }
     *             return constructor;
     *         }
     *     }
     *  yamlConstructors 类型是 HashMap key:Tag, value: SafeConstructor.ConstructYamlXXXX 计13种.
     *  接下来看 node.getTag() 可以看到它的结果 tag:yaml.org,2002:int 根据这个tag, 找到了SafeConstructor.ConstructYamlInt
     *
     *  接下来看看 tag 标签什么时候设置的?
     *  把断点打在 Node#setTay() 方法上, 断点停下来往前翻看,
     *   protected Node composeScalarNode(String anchor, List<CommentLine> blockComments) {
     *      nodeTag = resolver.resolve(NodeId.scalar, ev.getValue(),
     *                     ev.getImplicit().canOmitTagInPlainScalar());  // 这里是解析出tag
     *      Node node = new ScalarNode(nodeTag, resolved, ev.getValue(), ev.getStartMark(),
     *                 ev.getEndMark(), ev.getScalarStyle()); // 创建对象的时候,调用父类的构造方法,调用setTag方法
     *   }
     *   接下来进入 resolver.resolve 方法, 重要处理的逻辑, 获取第一个字符, 然后再获取对应的解析器
     *   resolvers = yamlImplicitResolvers.get(value.charAt(0)); // yamlImplicitResolvers map 中key是Character, value 是 List<ResolverTuple>
     *       // 如果找到解析器, 多个解析器遍历处理, 有匹配的立刻返回匹配的 tag
     *        if (resolvers != null) {
     *                 for (ResolverTuple v : resolvers) {
     *                     Tag tag = v.getTag();
     *                     Pattern regexp = v.getRegexp();
     *                     if (regexp.matcher(value).matches()) {
     *                         return tag;
     *                     }
     *                 }
     *             }
     *             // 如果没有获取到解析器, 尝试按照null 来处理
     *             if (yamlImplicitResolvers.containsKey(null)) {
     *                 for (ResolverTuple v : yamlImplicitResolvers.get(null)) {
     *                     Tag tag = v.getTag();
     *                     Pattern regexp = v.getRegexp();
     *                     if (regexp.matcher(value).matches()) {
     *                         return tag;
     *                     }
     *                 }
     *             }
     *             // 最后一个   switch (kind)  返回 str seq map
     *  ..... 经历层层返回来, 中间组装成子节点, 到这一行
     *  Node node = composer.getNode();
     *  下面是INT 类型的正则表达式, 以0开头 数字0-7 如果出现 8 9 就不匹配, 最后就是按照字符串处理
     *  public static final Pattern INT = Pattern
     *             .compile("^(?:" +
     *                     "[-+]?0b_*[0-1]+[0-1_]*" + // (base 2)
     *                     "|[-+]?0_*[0-7]+[0-7_]*" + // (base 8)
     *                     "|[-+]?(?:0|[1-9][0-9_]*)" + // (base 10)
     *                     "|[-+]?0x_*[0-9a-fA-F]+[0-9a-fA-F_]*" + // (base 16)
     *                     "|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+" + // (base 60)
     *                     ")$");
     * 解决办法: 由于解析过程中, 是根据第一个字符获取对应的解析器, 后续有可能匹配到其他的解析器造成解析结果不是我们期望的结果.
     * 如果只想要按照字符串的方式解析, 可以加上单引号|双引号(' ")都可以
     *
     *
     *
     */
    @Value("${pay.payId}")
    private int payId;

    @Value("${pay.version}")
    private String version;

    @Value("${pay.url}")
    private String url;

    public int getPayId() {
        return payId;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }


}
