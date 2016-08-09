# Zone
Android 文件型数据库

Swift版 看这里  https://github.com/yunyeLoveYoona/Zone-Swift/tree/master 

Zone是一个文件型数据库，针对业务逻辑较简单以及数据量级较小的Android Application。


Zone通过文件来存储数据，并且根据用户信息提供了不同的存储，使所有用户的数据都互相隔离。Zend通过对Model的反射来存储和读取数据，model
的任何成员变量的修改都不影响数据的操作，所以开发者不必为数据库升级而烦恼。

Zone在Application启动后会把数据加载到内存中，开发者的数据操作以及反馈都将首先在内存中进行然后再同步到文件中，提供了更快的数据操作
速度。

Zone提供了简单的用户数据切换api，只需一行代码就可以切换到不同的用户数据。

Zone提供了简单的API让开发者的使用尽可能的简单。

Zone提供了条件查询、limit以及排序。

Zong提供了表中存储的最大数据量设置，不用担心数据无限量的增加，Zone会删除最早添加的数据以维持一个有限的数据量。
