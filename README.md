# MVVM设计模式

相对于MVP设计模式，减少生命周期绑定（系统API已经做了处理），数据都通过`LiveData`进行绑定更新， 解耦繁重的`Presener`层；

### 基类建设

#### `BaseActivity `、`BaseFragment`、`BusinessActivity` 基类；

- 提供基础弹出框、输入框软键盘拦截等等；
- 绑定消息事件 `IMessageEvent`
- 全局业务处理等

#### `BaseViewModel` 

- 基础 `VM` 层，提供 `loading`、`message` 等通用的观察者类型 `MutableLiveData`；

```kotlin
open class BaseViewModel : ViewModel() {
    /**
     * 显示加载进度
     */
    val loading: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 文本信息显示
     */
    val message: MutableLiveData<String> = MutableLiveData()
}
```



### 网络框架建设

- 使用组件 `retrofit2`、`okhttp3`、`Flow`
- 支持REST风格规则
- 网络异常全局拦截机制
- 数据请求和响应数据绑定
- 支持Token刷新机制，通过 `Interceptor` 拦截重建`Request`、`Respone`



### 流程案例



### 流程案例

- #### 定义 `VM` 继承 `BaseViewModel`

  ```kotlin
  class MainViewModel : BaseViewModel() {
      fun requestVersion() {
          viewModelScope.launch {
              CommonRepository.requestCheckVersion(1000L, 1)
                  .onStart {
                      loading.value = true
                  }
                  .collect {
                      loading.value = false
                      when (it) {
                          is State.Success -> {
                              message.value = it.message
                          }
                          is State.Error -> {
                              message.value = it.message
                          }
                      }
                  }
          }
      }
  }
  ```

- #### 定义`Activity` 懒加载组件binding，委托 `viewModel`，数据只做监听

  ```kotlin
  // 根据当前泛型类型，顶层基类创建Presenter实例并和View进行绑定
  class MainActivity : BusinessActivity {
      private lateinit var binding: ActivityMainBinding
      private val viewModel: MainViewModel by viewModels()
      
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          // 绑定组件
          binding = ActivityMainBinding.inflate(layoutInflater)
          setContentView(binding.root)
          binding.fab.setOnClickListener { _ ->
              // 发起请求
              viewModel.requestVersion()
          }
  
          /**
           * 提示信息观察者
           */
          viewModel.message.observe(this) {
              Snackbar.make(binding.fab, it, Snackbar.LENGTH_LONG).show()
          }
  
          /**
           * Loading观察者
           */
          viewModel.loading.observe(this) {
              if (it) {
                  showDialog()
              } else {
                  dismissDialog()
              }
          }
      }
  }
  ```
  
  



