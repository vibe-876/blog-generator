;; Get it, Cam's Markup, camarkup. Hilarious, I know.
;; I didn't want to risk messing up someone's preexisting
;; markup-mode (I'm assuming that exists somewhere in the ether).


(defun camarkup-insert-link ()
  "Insert a camarkup style link at the
current position of the cursor.

This should only be called interactively."
  (interactive)
  (let ((address (read-string "U.R.I.: "))
	(text (read-string "Text to display: ")))
    (insert (concat "!" text "@" address "\n"))))

(defun camarkup-build-page (page-name &optional header-file body-file)
  "Builds a webpage using https://github.com/vibe-876/blog-generator.
This should not be used for anything else, or by anyone else, since it
relies on for specific non-public files I've written."
  (interactive)
  (let ((header (if (equal header-file nil) "header.html" header-file))
	(body (if (equal body-file nil) "body.html" body-file)))

    (shell-command (concat "java -jar bg.jar "
			   page-name " "
			   page-name ".html "
			   header " " body))

    (message "Done :3 .")))


(defvar-keymap camarkup-mode-map
  :doc "A keymap for `camarkup-mode'."
  :parent text-mode-map
  "C-c i" #'camarkup-insert-link
  "C-c C-c" #'camarkup-build-page)

(defcustom camarkup-mode-hook nil
  "Hook for `camarkup-mode'."
  :type 'hook)

(define-derived-mode camarkup-mode text-mode "camarkup"
  "Major mode for editing a specific kinda markup.
\\{camarkup-mode-map}
See https://github.com/vibe-876/blog-generator for
more details."
  (use-local-map camarkup-mode-map))

(add-to-list 'auto-mode-alist '("\\.cmu$" . camarkup-mode))
