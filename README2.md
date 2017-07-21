# Android_LED驱动移植
>### GPIO
```
一、什么是GPIO
GPIO，英文全称为General-Purpose IO ports，也就是通用IO口。
在微控制器芯片上一般都会提供一个“通用可编程IO接口”
```
>### GPIO实现灯的闪烁
```
kernel/drivers/leds/leds-msm-gpio-flash.c
利用这个文件来实现灯的灭、亮、慢闪、快闪
```
>>#### 注册这个设备
```
1.添加编译选项
//
修改​
//
kernel\drivers\leds\Makefile
添加
obj-y       += leds-msm-gpio-flash.o
2.修改配置文件
修改
kernel\arch\arm\configs\xxxx_defconfig
打开或添加
CONFIG_LEDS_MSM_GPIO_FLASH=y
3.注册平台驱动
修改
kernel\arch\arm\mach-msm\board-xxxx.c​​
这样就可以在/sys/class/leds/下注册设备节点了
```
>>#### 实现灯的闪烁
一是在modem端实现灯的逻辑控制
二是在Android端实现灯的控制
```
modem端是利用宏的传递:​
#define PCOM_CUSTOMER_CMD_LED_POWER_ON                1​
(void)msm_led_proc_comm(PCOM_CUSTOMER_CMD_LED_POWER_ON, LED_COLOR_RED);​
```

```
1.首先注册一个内核定时器
 struct timer_list led_test_timer;​
然后在设备初始化的时候初始化定时器
 init_timer(&led_test_timer)；
​ led_test_timer.data = (unsignedlong)0x00;
 led_test_timer.expires = jiffies + 0xFFFFFFFF*HZ;
 led_test_timer.function = led_test_timer_function;  //该函数按照自己的需要进行修改
 add_timer(&led_test_timer);
//
调用的时候修改定时器的时间
mod_timer(&reset_button_timer, jiffies + xx*HZ);
调用完成了再修改回去
​mod_timer(&reset_button_timer, jiffies + 0xFFFFFFFF*HZ);
2.然后注册一个新设备
static int msm_pmic_led_probe(struct platform_device *pdev)​
{
......
   rc = led_classdev_register(&pdev->dev, &led_test);​//添加
if (rc) {
   dev_err(&pdev->dev, "unable to register led_test driver\n");
   return rc;
}
......​
}​
//
static int __devexit msm_pmic_led_remove(struct platform_device *pdev)
{
......
   led_classdev_unregister(&led_test);//添加
......​
}​
//添加
static struct led_classdev msm_led_test = {
.name= "led_test",
.brightness_set= msm_led_test_set,
.brightness= 0,
};
//添加，灯的控制就在如下函数里面实现​
static void   msm_led_test_set(struct led_classdev *led_cdev,
enum led_brightness value)
{
        if(value == 0)//灭
        {
                mod_timer(&reset_button_timer, jiffies + 0xFFFFFFFF*HZ);
                value_now = 0;
        }​
        else if (value == 1)//亮
        {
                mod_timer(&reset_button_timer, jiffies + 0xFFFFFFFF*HZ);
                value_now = 1;
        }
        else if (value == 2)//慢闪
        {
​                 mod_timer(&reset_button_timer, jiffies + 0x1*HZ);//时间自己设定
                 value_now = 2;

        }
        else if (value == 3)//快闪
        {
​                  mod_timer(&reset_button_timer, jiffies + 0x2*HZ);//时间自己设定
                  value_now = 3;
        }
}​

向/sys/class/leds/led_test/brightness写入0,1,2,3,一个灯的各种灭、亮、慢闪、快闪就实现了
```
待续。。。