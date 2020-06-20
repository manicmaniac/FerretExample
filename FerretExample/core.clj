(require '[objc :as objc])
(require '[uikit :as ui])

(let [UIViewController (objc/objc-get-class "UIViewController")
      ViewController (objc/objc-allocate-class-pair UIViewController "ViewController", 0)]
  (objc/objc-register-class-pair ViewController)
  (objc/class-add-method ViewController
                         "viewDidLoad"
                         (objc/imp-implementation-with-ferret-lambda
                           (fn [self]
                             (let [view (objc/objc-msg-send self "view")
                                   UIColor (objc/objc-get-class "UIColor")
                                   green (objc/objc-msg-send UIColor "greenColor")]
                               (objc/objc-msg-send view "setBackgroundColor:" green)))
                           1)
                         "@:"))

(let [UIResponder (objc/objc-get-class "UIResponder")
      AppDelegate (objc/objc-allocate-class-pair UIResponder "AppDelegate", 0)]
  (objc/objc-register-class-pair AppDelegate)
  (objc/class-add-method AppDelegate
                         "application:didFinishLaunchingWithOptions:"
                         (objc/imp-implementation-with-ferret-lambda
                           (fn [self application options]
                             (let [UIWindow (objc/objc-get-class "UIWindow")
                                   window (objc/objc-msg-send (objc/objc-msg-send UIWindow "alloc") "init")]
                               (let [ViewController (objc/objc-get-class "ViewController")
                                     viewController (objc/objc-msg-send (objc/objc-msg-send ViewController "alloc") "init")
                                     UINavigationController (objc/objc-get-class "UINavigationController")
                                     navigationController (objc/objc-msg-send (objc/objc-msg-send UINavigationController "alloc") "initWithRootViewController:" viewController)]
                                 (objc/objc-msg-send window "setRootViewController:" navigationController))
                               (objc/objc-msg-send window "makeKeyAndVisible"))
                             true)
                           3)
                         "@:@@"))

(ui/application-main *command-line-args* "AppDelegate")
