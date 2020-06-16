(require '[objc :as objc])
(require '[uikit :as ui])

(objc/define-class "AppDelegate" "UIResponder")

(ui/application-main *command-line-args* "AppDelegate")
