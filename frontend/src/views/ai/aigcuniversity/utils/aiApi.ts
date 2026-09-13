import request from '@/config/axios'

interface Message {
  role: 'user' | 'assistant' | 'system'
  content: string
}

/** Requires a separately implemented authenticated server adapter. */
export async function callAiModel(messages: Message[]): Promise<string> {
  if (import.meta.env.VITE_AI_ENABLED !== 'true') {
    throw new Error('AI 服务未配置，请使用页面提供的基础建议。')
  }
  const url = import.meta.env.VITE_AI_PROXY_PATH || '/admin-api/ai/publication/chat'
  if (!url.startsWith('/') || url.startsWith('//')) {
    throw new Error('AI 代理地址必须是同源相对路径')
  }
  const result = await request.post({ url, data: { messages } })
  if (!result || typeof result.content !== 'string' || !result.content.trim()) {
    throw new Error('AI 服务没有返回有效内容')
  }
  return result.content
}
