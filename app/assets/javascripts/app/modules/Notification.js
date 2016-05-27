export const Level = {
  SUCCESS: 'success',
  ERROR: 'error',
  WARNING: 'warning',
  INFO: 'info'
}

export const Position = {
  TOP_RIGHT: 'tr',
  TOP_LEFT: 'tl',
  BOTTOM_RIGHT: 'br',
  BOTTOM_LEFT: 'bl',
  BOTTOM_CENTER: 'bc'
}

export const Duration = {
  INFINITE: 0,
  FAST: 2,
  MEDIUM: 5,
  SLOW: 8
}

export const notify = (title, message, level, position, duration) => {
  if(!(title && message && level)) return
  const t = title
  const m = message
  const l = level
  const p = position || Position.TOP_RIGHT
  const d = duration || Duration.MEDIUM
  window.UI.Notification.notify(t, m, l, p, d)
}

