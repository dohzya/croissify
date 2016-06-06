export const Locator = {
  home() {
    let route = Router.controllers.Website.index()
    location.href = route.absoluteURL()
  }
}
