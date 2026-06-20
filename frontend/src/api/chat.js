const BASE_URL = 'http://localhost:8083/api'

/**
 * 流式聊天 (SSE)
 * @param {string} message - 用户消息
 * @param {string} sessionId - 会话ID
 * @param {object} callbacks - { onToken, onSources, onDone, onError }
 * @returns {AbortController} - 用于取消请求
 */
export function chatStream(message, sessionId, callbacks) {
  const controller = new AbortController()

  fetch(`${BASE_URL}/chat/stream`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ message, sessionId }),
    signal: controller.signal
  }).then(async response => {
    if (!response.ok) {
      callbacks.onError?.(`HTTP ${response.status}`)
      return
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder()
    let buffer = ''

    while (true) {
      const { done, value } = await reader.read()
      if (done) break

      buffer += decoder.decode(value, { stream: true })
      const lines = buffer.split('\n')
      buffer = lines.pop() || ''

      for (const line of lines) {
        if (line.startsWith('event:')) {
          const eventName = line.slice(6).trim()
          // 下一行应该是 data: 行
          continue
        }

        if (!line.startsWith('data:')) continue

        const data = line.slice(5).trim()

        if (data === '[DONE]') {
          callbacks.onDone?.()
          return
        }

        // 尝试解析 JSON（sources 事件）
        try {
          const parsed = JSON.parse(data)
          if (parsed.type === 'sources') {
            callbacks.onSources?.(parsed.data)
            continue
          }
        } catch {
          // 不是 JSON，就是普通 token
        }

        callbacks.onToken?.(data)
      }
    }

    callbacks.onDone?.()
  }).catch(err => {
    if (err.name !== 'AbortError') {
      callbacks.onError?.(err.message || '网络请求失败')
    }
  })

  return controller
}

/**
 * 同步聊天
 */
export async function chat(message, sessionId) {
  const response = await fetch(`${BASE_URL}/chat`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ message, sessionId })
  })
  return response.json()
}
