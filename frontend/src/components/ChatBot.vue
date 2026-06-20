<template>
  <Teleport to="body">
    <!-- 聊天窗口 -->
    <Transition name="chat-fade">
      <div v-if="isOpen" ref="chatWindowRef" class="chat-window" :class="{ dark: isDark }" :style="windowStyle">
        <div class="chat-header">
          <div class="chat-header-info">
            <div class="chat-avatar-small">
              <svg viewBox="0 0 32 32" width="20" height="20" fill="none">
                <circle cx="16" cy="3" r="1.5" fill="white"/>
                <line x1="16" y1="4.5" x2="16" y2="7" stroke="white" stroke-width="1.5" stroke-linecap="round"/>
                <rect x="6" y="7" width="20" height="13" rx="4" stroke="white" stroke-width="1.5"/>
                <rect x="9" y="10" width="14" height="6" rx="2" stroke="white" stroke-width="1" opacity="0.5"/>
                <circle cx="13" cy="13" r="1.5" fill="white"/>
                <circle cx="19" cy="13" r="1.5" fill="white"/>
                <rect x="9" y="21" width="14" height="6" rx="2" stroke="white" stroke-width="1.5"/>
                <circle cx="16" cy="24" r="1.5" fill="white"/>
                <rect x="3" y="11" width="3" height="5" rx="1.5" fill="white"/>
                <rect x="26" y="11" width="3" height="5" rx="1.5" fill="white"/>
                <rect x="10" y="27.5" width="4" height="2" rx="1" fill="white"/>
                <rect x="18" y="27.5" width="4" height="2" rx="1" fill="white"/>
              </svg>
            </div>
            <span>AI 助手</span>
          </div>
          <button class="chat-close" @click="isOpen = false">&times;</button>
        </div>

        <div class="chat-body" ref="chatBodyRef">
          <div v-if="messages.length === 0" class="chat-welcome">
            <svg viewBox="0 0 32 32" width="48" height="48" fill="none">
              <circle cx="16" cy="3" r="1.5" fill="#667eea"/>
              <line x1="16" y1="4.5" x2="16" y2="7" stroke="#667eea" stroke-width="1.5" stroke-linecap="round"/>
              <rect x="6" y="7" width="20" height="13" rx="4" stroke="#667eea" stroke-width="1.5"/>
              <rect x="9" y="10" width="14" height="6" rx="2" stroke="#667eea" stroke-width="1" opacity="0.5"/>
              <circle cx="13" cy="13" r="1.5" fill="#667eea"/>
              <circle cx="19" cy="13" r="1.5" fill="#667eea"/>
              <rect x="9" y="21" width="14" height="6" rx="2" stroke="#667eea" stroke-width="1.5"/>
              <circle cx="16" cy="24" r="1.5" fill="#667eea"/>
              <rect x="3" y="11" width="3" height="5" rx="1.5" fill="#667eea"/>
              <rect x="26" y="11" width="3" height="5" rx="1.5" fill="#667eea"/>
              <rect x="10" y="27.5" width="4" height="2" rx="1" fill="#667eea"/>
              <rect x="18" y="27.5" width="4" height="2" rx="1" fill="#667eea"/>
            </svg>
            <p>你好！我是博客 AI 助手</p>
            <span>试试问我关于博客内容的问题吧~</span>
          </div>

          <div v-for="(msg, idx) in messages" :key="idx" class="chat-msg" :class="msg.role">
            <div v-if="msg.role === 'ai'" class="msg-avatar">
              <svg viewBox="0 0 32 32" width="18" height="18" fill="none">
                <circle cx="16" cy="3" r="1.5" fill="#a78bfa"/>
                <line x1="16" y1="4.5" x2="16" y2="7" stroke="#a78bfa" stroke-width="1.5" stroke-linecap="round"/>
                <rect x="6" y="7" width="20" height="13" rx="4" stroke="#a78bfa" stroke-width="1.5"/>
                <rect x="9" y="10" width="14" height="6" rx="2" stroke="#a78bfa" stroke-width="1" opacity="0.5"/>
                <circle cx="13" cy="13" r="1.5" fill="#a78bfa"/>
                <circle cx="19" cy="13" r="1.5" fill="#a78bfa"/>
                <rect x="9" y="21" width="14" height="6" rx="2" stroke="#a78bfa" stroke-width="1.5"/>
                <circle cx="16" cy="24" r="1.5" fill="#a78bfa"/>
                <rect x="3" y="11" width="3" height="5" rx="1.5" fill="#a78bfa"/>
                <rect x="26" y="11" width="3" height="5" rx="1.5" fill="#a78bfa"/>
                <rect x="10" y="27.5" width="4" height="2" rx="1" fill="#a78bfa"/>
                <rect x="18" y="27.5" width="4" height="2" rx="1" fill="#a78bfa"/>
              </svg>
            </div>
            <div class="msg-bubble">
              <div class="msg-text" v-html="formatText(msg.text)"></div>
              <div v-if="msg.sources && msg.sources.length" class="msg-sources">
                <span class="sources-label">参考文章：</span>
                <a v-for="src in msg.sources" :key="src.articleId"
                   :href="`/article/${src.articleId}`"
                   class="source-link"
                   target="_blank">
                  {{ src.title }}
                </a>
              </div>
            </div>
          </div>

          <div v-if="isLoading" class="chat-msg ai">
            <div class="msg-avatar">
              <svg viewBox="0 0 32 32" width="18" height="18" fill="none">
                <circle cx="16" cy="3" r="1.5" fill="#a78bfa"/>
                <line x1="16" y1="4.5" x2="16" y2="7" stroke="#a78bfa" stroke-width="1.5" stroke-linecap="round"/>
                <rect x="6" y="7" width="20" height="13" rx="4" stroke="#a78bfa" stroke-width="1.5"/>
                <rect x="9" y="10" width="14" height="6" rx="2" stroke="#a78bfa" stroke-width="1" opacity="0.5"/>
                <circle cx="13" cy="13" r="1.5" fill="#a78bfa"/>
                <circle cx="19" cy="13" r="1.5" fill="#a78bfa"/>
                <rect x="9" y="21" width="14" height="6" rx="2" stroke="#a78bfa" stroke-width="1.5"/>
                <circle cx="16" cy="24" r="1.5" fill="#a78bfa"/>
                <rect x="3" y="11" width="3" height="5" rx="1.5" fill="#a78bfa"/>
                <rect x="26" y="11" width="3" height="5" rx="1.5" fill="#a78bfa"/>
                <rect x="10" y="27.5" width="4" height="2" rx="1" fill="#a78bfa"/>
                <rect x="18" y="27.5" width="4" height="2" rx="1" fill="#a78bfa"/>
              </svg>
            </div>
            <div class="msg-bubble">
              <div v-if="streamText" class="msg-text" v-html="formatText(streamText)"></div>
              <div v-else class="typing-dots">
                <span></span><span></span><span></span>
              </div>
              <div v-if="currentSources.length" class="msg-sources">
                <span class="sources-label">参考文章：</span>
                <a v-for="src in currentSources" :key="src.articleId"
                   :href="`/article/${src.articleId}`"
                   class="source-link"
                   target="_blank">
                  {{ src.title }}
                </a>
              </div>
            </div>
          </div>
        </div>

        <div class="chat-footer">
          <textarea
            ref="inputRef"
            v-model="inputText"
            placeholder="输入你的问题..."
            @keydown="handleKeydown"
            rows="1"
          ></textarea>
          <button class="send-btn" :disabled="!inputText.trim() || isLoading" @click="sendMessage">
            <svg viewBox="0 0 24 24" width="18" height="18" fill="currentColor">
              <path d="M2.01 21L23 12 2.01 3 2 10l15 2-15 2z"/>
            </svg>
          </button>
        </div>
      </div>
    </Transition>

    <!-- 可拖拽机器人按钮 -->
    <div
      class="chat-bot-btn"
      :class="{ dark: isDark }"
      :style="{ left: btnPos.x + 'px', top: btnPos.y + 'px' }"
      ref="botBtnRef"
      @mousedown="onDragStart"
      @click="onBtnClick"
      @dblclick="onBtnDblClick"
    >
      <svg viewBox="0 0 32 32" width="46" height="46" fill="none">
        <defs>
          <linearGradient id="btnRobotGrad" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" stop-color="#667eea"/>
            <stop offset="100%" stop-color="#764ba2"/>
          </linearGradient>
        </defs>
        <circle cx="16" cy="3" r="1.5" fill="url(#btnRobotGrad)"/>
        <line x1="16" y1="4.5" x2="16" y2="7" stroke="url(#btnRobotGrad)" stroke-width="1.5" stroke-linecap="round"/>
        <rect x="6" y="7" width="20" height="13" rx="4" stroke="url(#btnRobotGrad)" stroke-width="1.5"/>
        <rect x="9" y="10" width="14" height="6" rx="2" stroke="url(#btnRobotGrad)" stroke-width="1" opacity="0.5"/>
        <circle cx="13" cy="13" r="1.5" fill="url(#btnRobotGrad)"/>
        <circle cx="19" cy="13" r="1.5" fill="url(#btnRobotGrad)"/>
        <rect x="9" y="21" width="14" height="6" rx="2" stroke="url(#btnRobotGrad)" stroke-width="1.5"/>
        <circle cx="16" cy="24" r="1.5" fill="url(#btnRobotGrad)"/>
        <rect x="3" y="11" width="3" height="5" rx="1.5" fill="url(#btnRobotGrad)"/>
        <rect x="26" y="11" width="3" height="5" rx="1.5" fill="url(#btnRobotGrad)"/>
        <rect x="10" y="27.5" width="4" height="2" rx="1" fill="url(#btnRobotGrad)"/>
        <rect x="18" y="27.5" width="4" height="2" rx="1" fill="url(#btnRobotGrad)"/>
      </svg>
    </div>

    <!-- 提示气泡 -->
    <Transition name="bubble">
      <div
        v-if="showBubble && !isOpen"
        class="chat-hint-bubble"
        :class="{ dark: isDark }"
        :style="bubbleStyle"
        @click="onBubbleClick"
      >
        <span class="bubble-text">{{ bubbleText }}</span>
        <div class="bubble-tail"></div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
import { ref, computed, nextTick, onMounted, onUnmounted } from 'vue'
import { chatStream } from '@/api/chat'

const isOpen = ref(false)
const inputText = ref('')
const messages = ref([])
const isLoading = ref(false)
const streamText = ref('')
const currentSources = ref([])
const chatBodyRef = ref(null)
const inputRef = ref(null)
const chatWindowRef = ref(null)
const botBtnRef = ref(null)
const sessionId = ref('session_' + Date.now())

let abortController = null

// 拖拽相关
const btnPos = ref({ x: 20, y: window.innerHeight - 80 })
let dragStart = null
let hasMoved = false

// 提示气泡
const showBubble = ref(false)
const bubbleText = ref('')
const bubbleDisabled = ref(false)
let bubbleIndex = 0
let bubbleInterval = null
let bubbleHideTimer = null
const BUBBLE_SHOW_DURATION = 4000    // 气泡显示时长
const BUBBLE_INTERVAL = 10000        // 气泡出现间隔
const bubbleHints = [
  '💬 有问题？点击我聊聊吧~',
  '🤖 我是 AI 助手，有什么想问的？',
  '✨ 点击我，开始智能问答！',
  '📖 对博客内容有疑问？问我吧！',
  '💡 双击侧栏头像可关闭此提示',
  '🔍 想了解什么？我来帮你找答案~',
]
const BUBBLE_MAX_W = 220
const bubbleStyle = computed(() => {
  const vw = window.innerWidth
  const vh = window.innerHeight
  const bx = btnPos.value.x, by = btnPos.value.y
  // 优先在按钮右侧，否则左侧
  let left
  if (bx + BTN + BUBBLE_MAX_W + 16 > vw) {
    left = bx - BUBBLE_MAX_W - 8
  } else {
    left = bx + BTN + 8
  }
  left = Math.max(8, Math.min(vw - BUBBLE_MAX_W - 8, left))
  // 垂直居中于按钮
  let top = by + BTN / 2 - 18
  top = Math.max(8, Math.min(vh - 50, top))
  // 尾巴方向
  const tailLeft = left < bx
  return {
    left: left + 'px',
    top: top + 'px',
    '--tail-left': tailLeft ? 'auto' : '-6px',
    '--tail-right': tailLeft ? '-6px' : 'auto',
    '--tail-border-left': tailLeft ? 'none' : '1px solid #e0e0e0',
    '--tail-border-right': tailLeft ? '1px solid #e0e0e0' : 'none',
  }
})

const isDark = ref(
  document.documentElement.getAttribute('data-theme') === 'dark'
  || window.matchMedia('(prefers-color-scheme: dark)').matches
)
let themeObserver = null

// 聊天窗口位置跟随机器人按钮
const WIN_W = 380, WIN_H = 520, BTN = 56, GAP = 12
const windowStyle = computed(() => {
  const vw = window.innerWidth
  const vh = window.innerHeight
  const bx = btnPos.value.x
  const by = btnPos.value.y

  // 垂直：优先在按钮上方，否则下方
  let top
  if (by + BTN + GAP + WIN_H > vh) {
    top = by - GAP - WIN_H  // 按钮上方
  } else {
    top = by + BTN + GAP    // 按钮下方
  }

  // 水平：优先左对齐按钮，否则右对齐
  let left
  if (bx + WIN_W > vw) {
    left = bx + BTN - WIN_W  // 右对齐按钮
  } else {
    left = bx                // 左对齐按钮
  }

  // 确保不超出视口
  left = Math.max(8, Math.min(vw - WIN_W - 8, left))
  top = Math.max(8, Math.min(vh - WIN_H - 8, top))

  return { left: left + 'px', top: top + 'px' }
})

function onDragStart(e) {
  dragStart = { x: e.clientX - btnPos.value.x, y: e.clientY - btnPos.value.y }
  hasMoved = false
  document.addEventListener('mousemove', onDragMove)
  document.addEventListener('mouseup', onDragEnd)
}

function onDragMove(e) {
  if (!dragStart) return
  const newX = e.clientX - dragStart.x
  const newY = e.clientY - dragStart.y
  if (Math.abs(newX - btnPos.value.x) > 5 || Math.abs(newY - btnPos.value.y) > 5) {
    hasMoved = true
  }
  // 限制在视口内
  btnPos.value.x = Math.max(0, Math.min(window.innerWidth - BTN, newX))
  btnPos.value.y = Math.max(0, Math.min(window.innerHeight - BTN, newY))
}

function onDragEnd() {
  dragStart = null
  document.removeEventListener('mousemove', onDragMove)
  document.removeEventListener('mouseup', onDragEnd)
}

function onBtnClick() {
  if (hasMoved) return
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    hideBubble()
    nextTick(() => inputRef.value?.focus())
  }
}

function onBtnDblClick() {
  // 双击归位到左下角初始位置
  btnPos.value = { x: 20, y: window.innerHeight - 80 }
}

function onBubbleClick() {
  hideBubble()
  isOpen.value = true
  nextTick(() => inputRef.value?.focus())
}

function showNextBubble() {
  if (isOpen.value || bubbleDisabled.value) return
  bubbleText.value = bubbleHints[bubbleIndex % bubbleHints.length]
  bubbleIndex++
  showBubble.value = true
  // 自动隐藏
  clearTimeout(bubbleHideTimer)
  bubbleHideTimer = setTimeout(() => {
    showBubble.value = false
  }, BUBBLE_SHOW_DURATION)
}

function hideBubble() {
  showBubble.value = false
  clearTimeout(bubbleHideTimer)
}

function handleKeydown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

function sendMessage() {
  const text = inputText.value.trim()
  if (!text || isLoading.value) return

  messages.value.push({ role: 'user', text })
  inputText.value = ''
  isLoading.value = true
  streamText.value = ''
  currentSources.value = []

  scrollToBottom()

  abortController = chatStream(text, sessionId.value, {
    onToken(token) {
      streamText.value += token
      scrollToBottom()
    },
    onSources(sources) {
      currentSources.value = sources || []
    },
    onDone() {
      if (streamText.value) {
        messages.value.push({
          role: 'ai',
          text: streamText.value,
          sources: currentSources.value
        })
      }
      streamText.value = ''
      currentSources.value = []
      isLoading.value = false
      abortController = null
      scrollToBottom()
    },
    onError(err) {
      if (streamText.value) {
        messages.value.push({
          role: 'ai',
          text: streamText.value,
          sources: currentSources.value
        })
      } else {
        messages.value.push({
          role: 'ai',
          text: '抱歉，AI 服务暂时不可用，请稍后重试。'
        })
      }
      streamText.value = ''
      currentSources.value = []
      isLoading.value = false
      abortController = null
      scrollToBottom()
    }
  })
}

function scrollToBottom() {
  nextTick(() => {
    if (chatBodyRef.value) {
      chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
    }
  })
}

function formatText(text) {
  if (!text) return ''
  // 简单的 Markdown 格式处理
  return text
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/`([^`]+)`/g, '<code>$1</code>')
    .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
    .replace(/\n/g, '<br>')
}

function onBubbleToggle(e) {
  bubbleDisabled.value = e.detail.disabled
  if (bubbleDisabled.value) {
    hideBubble()
    clearInterval(bubbleInterval)
    bubbleInterval = null
  } else if (!bubbleInterval) {
    // 重新开启：立即显示一次，然后恢复定时器
    showNextBubble()
    bubbleInterval = setInterval(showNextBubble, BUBBLE_INTERVAL)
  }
}

// 监听窗口大小变化
function onResize() {
  btnPos.value.x = Math.min(btnPos.value.x, window.innerWidth - BTN)
  btnPos.value.y = Math.min(btnPos.value.y, window.innerHeight - BTN)
}

// 点击外部关闭聊天窗口
function onClickOutside(e) {
  if (!isOpen.value) return
  const win = chatWindowRef.value
  const btn = botBtnRef.value
  if (win && win.contains(e.target)) return
  if (btn && btn.contains(e.target)) return
  isOpen.value = false
}

onMounted(() => {
  window.addEventListener('resize', onResize)
  window.addEventListener('chatBubbleToggle', onBubbleToggle)
  document.addEventListener('mousedown', onClickOutside)
  // 重新同步主题状态（此时 Navbar 已完成初始化）
  isDark.value = document.documentElement.getAttribute('data-theme') === 'dark'
  // 刷新页面时重置气泡状态为开启
  localStorage.removeItem('chatBubbleDisabled')
  // 监听 data-theme 属性变化
  themeObserver = new MutationObserver(() => {
    isDark.value = document.documentElement.getAttribute('data-theme') === 'dark'
  })
  themeObserver.observe(document.documentElement, {
    attributes: true,
    attributeFilter: ['data-theme']
  })
  // 提示气泡：首次延迟 3 秒出现，之后每 10 秒出现一次
  if (!bubbleDisabled.value) {
    setTimeout(() => {
      showNextBubble()
      bubbleInterval = setInterval(showNextBubble, BUBBLE_INTERVAL)
    }, 3000)
  }
})
onUnmounted(() => {
  window.removeEventListener('resize', onResize)
  window.removeEventListener('chatBubbleToggle', onBubbleToggle)
  document.removeEventListener('mousedown', onClickOutside)
  themeObserver?.disconnect()
  clearInterval(bubbleInterval)
  clearTimeout(bubbleHideTimer)
  abortController?.abort()
})
</script>

<style scoped>
/* 机器人按钮 */
.chat-bot-btn {
  position: fixed;
  width: 56px;
  height: 56px;
  background: transparent;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 9999;
  user-select: none;
  transition: transform 0.2s;
  filter: drop-shadow(0 2px 6px rgba(102, 126, 234, 0.35));
}

.chat-bot-btn:hover {
  transform: scale(1.12);
  filter: drop-shadow(0 3px 10px rgba(102, 126, 234, 0.5));
}

.chat-bot-btn.dark {
  filter: drop-shadow(0 2px 8px rgba(102, 126, 234, 0.5));
}

/* 聊天窗口 */
.chat-window {
  position: fixed;
  width: 380px;
  height: 520px;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  z-index: 9998;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.chat-window.dark {
  background: #1e1e2e;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.4);
}

/* Header */
.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.dark .chat-header {
  background: linear-gradient(135deg, #4a5a9a 0%, #5a3a8a 100%);
}

.chat-header-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 15px;
}

.chat-avatar-small {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
}

.chat-close {
  background: none;
  border: none;
  color: white;
  font-size: 22px;
  cursor: pointer;
  padding: 0 4px;
  opacity: 0.8;
  transition: opacity 0.2s;
}

.chat-close:hover {
  opacity: 1;
}

/* Body */
.chat-body {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  scroll-behavior: smooth;
}

.chat-body::-webkit-scrollbar {
  width: 4px;
}

.chat-body::-webkit-scrollbar-thumb {
  background: #d1d5db;
  border-radius: 2px;
}

.dark .chat-body::-webkit-scrollbar-thumb {
  background: #45475a;
}

/* Welcome */
.chat-welcome {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px 20px;
  color: #666;
  text-align: center;
  gap: 8px;
}

.chat-welcome p {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.dark .chat-welcome p {
  color: #eaeaea;
}

.chat-welcome span {
  font-size: 13px;
  color: #999;
}

.dark .chat-welcome span {
  color: #888;
}

/* Messages */
.chat-msg {
  display: flex;
  gap: 8px;
  max-width: 100%;
}

.chat-msg.user {
  justify-content: flex-end;
}

.chat-msg.ai {
  justify-content: flex-start;
}

.msg-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #f3f0ff;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  margin-top: 2px;
}

.dark .msg-avatar {
  background: #2a2a3e;
}

.msg-bubble {
  max-width: 75%;
  padding: 10px 14px;
  border-radius: 14px;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}

.chat-msg.user .msg-bubble {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.chat-msg.ai .msg-bubble {
  background: #f3f4f6;
  color: #333;
  border-bottom-left-radius: 4px;
}

.dark .chat-msg.ai .msg-bubble {
  background: #2a2a3e;
  color: #e0e0e0;
}

.msg-text :deep(code) {
  background: rgba(0, 0, 0, 0.06);
  padding: 1px 5px;
  border-radius: 3px;
  font-size: 13px;
  font-family: 'Consolas', monospace;
}

.dark .msg-text :deep(code) {
  background: rgba(255, 255, 255, 0.1);
}

.chat-msg.user .msg-text :deep(code) {
  background: rgba(255, 255, 255, 0.2);
}

/* Sources */
.msg-sources {
  margin-top: 8px;
  padding-top: 8px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  align-items: center;
}

.dark .msg-sources {
  border-top-color: rgba(255, 255, 255, 0.1);
}

.sources-label {
  font-size: 11px;
  color: #999;
  margin-right: 4px;
}

.source-link {
  font-size: 12px;
  color: #667eea;
  text-decoration: none;
  padding: 2px 8px;
  background: rgba(102, 126, 234, 0.08);
  border-radius: 10px;
  transition: background 0.2s;
}

.source-link:hover {
  background: rgba(102, 126, 234, 0.2);
}

.dark .source-link {
  color: #a0b0f0;
  background: rgba(102, 126, 234, 0.15);
}

/* Typing dots */
.typing-dots {
  display: flex;
  gap: 4px;
  padding: 4px 0;
}

.typing-dots span {
  width: 6px;
  height: 6px;
  background: #999;
  border-radius: 50%;
  animation: dot-bounce 1.4s infinite ease-in-out;
}

.typing-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes dot-bounce {
  0%, 80%, 100% { transform: translateY(0); opacity: 0.4; }
  40% { transform: translateY(-6px); opacity: 1; }
}

/* Footer */
.chat-footer {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  padding: 12px 16px;
  border-top: 1px solid #f0f0f0;
}

.dark .chat-footer {
  border-top-color: #2a2a4e;
}

.chat-footer textarea {
  flex: 1;
  resize: none;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 8px 12px;
  font-size: 14px;
  line-height: 1.4;
  outline: none;
  background: #f9fafb;
  color: #333;
  transition: border-color 0.2s;
  max-height: 80px;
  font-family: inherit;
}

.dark .chat-footer textarea {
  background: #252535;
  border-color: #3a3a5a;
  color: #e0e0e0;
}

.chat-footer textarea:focus {
  border-color: #667eea;
}

.chat-footer textarea::placeholder {
  color: #aaa;
}

.send-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: none;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  transition: opacity 0.2s;
}

.send-btn:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.send-btn:not(:disabled):hover {
  opacity: 0.9;
}

.dark .send-btn {
  background: linear-gradient(135deg, #4a5a9a 0%, #5a3a8a 100%);
}

/* Transition */
.chat-fade-enter-active,
.chat-fade-leave-active {
  transition: opacity 0.25s, transform 0.25s;
}

.chat-fade-enter-from,
.chat-fade-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.95);
}

/* 提示气泡 */
.chat-hint-bubble {
  position: fixed;
  z-index: 9998;
  max-width: 220px;
  padding: 10px 14px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.15), 0 1px 4px rgba(0, 0, 0, 0.08);
  cursor: pointer;
  pointer-events: auto;
  animation: bubble-wobble 3s ease-in-out infinite;
}

.chat-hint-bubble.dark {
  background: #2a2a4e;
  box-shadow: 0 4px 20px rgba(102, 126, 234, 0.25), 0 1px 4px rgba(0, 0, 0, 0.3);
}

.bubble-text {
  font-size: 13px;
  line-height: 1.5;
  color: #333;
  white-space: nowrap;
}

.chat-hint-bubble.dark .bubble-text {
  color: #e0e0e0;
}

.bubble-tail {
  position: absolute;
  top: 50%;
  transform: translateY(-50%) rotate(45deg);
  width: 10px;
  height: 10px;
  background: #fff;
  left: var(--tail-left);
  right: var(--tail-right);
  border-left: var(--tail-border-left);
  border-bottom: var(--tail-border-left);
  border-right: var(--tail-border-right);
  border-top: var(--tail-border-right);
}

.chat-hint-bubble.dark .bubble-tail {
  background: #2a2a4e;
}

/* 暗色模式下尾巴边框颜色覆盖 */
.chat-hint-bubble.dark {
  --tail-border-left: 1px solid #3a3a5e;
  --tail-border-right: 1px solid #3a3a5e;
}

@keyframes bubble-wobble {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}

/* 气泡过渡动画 */
.bubble-enter-active {
  transition: opacity 0.35s ease, transform 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
}

.bubble-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.bubble-enter-from {
  opacity: 0;
  transform: translateX(-10px) scale(0.85);
}

.bubble-leave-to {
  opacity: 0;
  transform: translateX(-10px) scale(0.85);
}

/* 响应式 */
@media (max-width: 480px) {
  .chat-window {
    width: calc(100vw - 20px);
    height: calc(100vh - 120px);
    border-radius: 12px;
  }
}
</style>
