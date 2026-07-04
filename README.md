# Inject Ref

Inject Ref 提供两个独立插件：一个用于基于 VS Code 开发的 IDE（例如 Cursor、Trae等)，一个用于 JetBrains 系列 IDE。

它们提供同一个命令：`Copy AI Relative Path`。执行后会复制当前文件或目录的项目相对路径，并在前面加上 `@`；如果路径以 `.` 开头(这些文件一般是被忽略的本地文件，Agent 工具不会自动识别，强行加反而可能会造成额外的烦恼)，则保持原样

```text
@src/components/Button.tsx
.env
.local/runbooks/release.md
```

## 为什么做这个插件

通常我在 Vibe Coding 时，都会开一个 IDE 用于代码和文档的审查。有时候让 AI 读某份代码或者某个文件夹时会有以下方式：

1. 直接在 Codex/Claude 中使用 `@` 来搜索并选择文件/文件夹
2. 在 IDE 中使用快捷键复制绝对/相对路径粘贴到 Codex/Claude 中

我平时用的最多是第二种，虽然直接贴绝对/相对路径，AI 也能识别，但是 Codex/Claude 对加了 `@` 的路径有自动包装成超链接的短链形式。

所以偶尔我还是会手动加个 `@` 来让路径变成短链，用久了感觉手动加有点麻烦，就顺手 Vibe 了个插件自动加 `@`。

## 本地编译安装

这个仓库不发布到插件市场。需要使用时，从源码编译本地安装包。

先克隆仓库：

```bash
git clone https://github.com/LetTTGACO/inject-ref.git
cd inject-ref
```

### 编译 VS Code 插件

需要本机已安装 Node.js 和 npm。

```bash
cd vscode
npm install
npm test
npx @vscode/vsce package
```

完成后会在 `vscode/` 目录生成 `.vsix` 文件，例如：

```text
inject-ref-vscode-0.1.2.vsix
```

安装方式二选一：

```bash
code --install-extension inject-ref-vscode-0.1.2.vsix
```

或者在 VS Code 的 Extensions 面板中选择 `Install from VSIX...`，再选择生成的 `.vsix` 文件。

### 编译 JetBrains 系列 IDE 插件

```bash
cd jetbrains
./gradlew test buildPlugin
```

完成后会在 `jetbrains/build/distributions/` 目录生成插件 zip 包。

安装方式：

1. 打开 IDE Settings。
2. 进入 Plugins。
3. 点击齿轮菜单。
4. 选择 `Install Plugin from Disk...`。
5. 选择 `jetbrains/build/distributions/` 里的 zip 包。
6. 按提示重启 IDE。

## 插件不设置默认快捷键

每个人的快捷键习惯不同。Inject Ref 不预设默认快捷键，也不会拦截 IDE 原生命令。

如果你想保留原来的操作习惯，可以把自己原本用于“复制相对路径”的快捷键绑定到 `Copy AI Relative Path`。

## VS Code 使用

安装 VS Code 插件后：

1. 打开 Command Palette。
2. 搜索 `Copy AI Relative Path`。
3. 执行命令后，剪贴板会得到处理后的项目相对路径。

绑定快捷键：

1. 打开 Keyboard Shortcuts。
2. 搜索 `Copy AI Relative Path`。
3. 绑定你原本用于“复制相对路径”的快捷键。

插件也会在 Explorer 右键菜单中提供 `Copy AI Relative Path`。

在 Explorer 里使用快捷键时，需要先让 Explorer 中的文件或目录处于选中状态。此时插件会复用 VS Code 原生 `Copy Relative Path` 的选择逻辑，再把结果改写为 `@` 开头的引用。

## JetBrains 系列 IDE 使用

安装插件后：

1. 在 Project 视图或编辑器中选择文件或目录。
2. 执行 `Copy AI Relative Path`。
3. 剪贴板会得到处理后的项目相对路径。

绑定快捷键：

1. 打开 Settings。
2. 进入 Keymap。
3. 搜索 `Copy AI Relative Path`。
4. 绑定你原本用于“复制相对路径”的快捷键。

使用快捷键时，需要先让 Project 视图中的文件或目录处于选中状态，并且当前焦点在 Project 视图或对应编辑器上下文中。仅把鼠标悬停在左侧文件树上，不会改变 IDE 的快捷键上下文。

如果这个快捷键同时绑定在 IDE 原生的复制路径动作上，请在 Keymap 的冲突提示中移除或改绑原来的动作，只保留 `Copy AI Relative Path`。

## 不做什么

- 不监听剪贴板。
- 不拦截原生复制命令。
- 不设置默认快捷键。
