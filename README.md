# firekaka
本项目是 [Robinson](https://github.com/mbrubeck/robinson) 的 java 版本，已经实现了原项目的全部功能。

这是一个十分简易的 web 渲染引擎，并不是一个真正可用的项目，可以作为学习 html 和 css 的练手项目。
目前支持的功能：
- html: DOM 树中会生成对应的文字节点，但不支持渲染
- css: 支持标签、类、id及它们组合的选择器 
- 渲染: 目前只能渲染 CSS 2 中定义的 Block 布局
### 预览
html文件：

    <div class="a">
      <div class="b">
        <div class="c">
          <div class="d">
            <div class="e">
              <div class="f">
                <div class="g">
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

css 文件：

    div {
    display: block;
    padding-right: 12px;
    padding-left: 12px;
    padding-top: 12px;
    padding-bottom: 12px;
    }
    .a { background: #ff0000; }
    .b { background: #ffa500; }
    .c { background: #ffff00; }
    .d { background: #008000; }
    .e { background: #0000ff; }
    .f { background: #4b0082; }
    .g { background: #800080; }


生成页面图片：

![生成图片](./res/website/website.png)
### 如何使用
直接克隆本仓库，src/test 文件夹下有各个功能对应的测试类，src/res下存放对应的测试文件。