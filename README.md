# 数据抓取


## 运行环境
    JDK8+
    Tomcat8+
    Mysql5+
    
## 软件版本
    spring-boot 1.5.10.RELEASE


## 数据抓取策略

    1、先看列表页和详情页的内容是否规律

    2、先测试抓取列表页和详情页的数据，看是否有限制等

    3、如果有限制，则仔细分析浏览器请求时的异同，也可抓包进行分析

    4、模拟浏览器行为，再次进行数据抓取

    5、最少要弄四个互不影响、相互独立的功能：专门抓取列表页、专门解析列表页、专门抓取详情页、专门解析详情页

    6、如果有条件，则装个动态IP代理的软件，每隔一段时间换一次ip

    7、如有访问频次的限制，则每隔一段时间需要停止抓取（可自定义、可随机）
    
    8、如果ip被封，而且动态代理也不起作用，则可能需要另外找机器去抓取
    
    9、查看要抓取的网页，使用get 和 post的方式有何差异。有的网站对get方式无限制

    10、如果再抓取数据的几周后，突然抓不到了，但浏览器还能浏览要爬的网页，就得再次抓包分析



## TODO LIST

    * 遗漏的今年数据
    * 乱码问题
    * 抓取省市区数据，包括编号
    * 从所有已抓取的数据中，解析出省市区信息并保存、解析颁发公告的单位名称并保存


    test