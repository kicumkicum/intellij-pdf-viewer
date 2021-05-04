package com.firsttimeinforever.intellij.pdf.viewer.application.pdfjs

import com.firsttimeinforever.intellij.pdf.viewer.application.pdfjs.types.PdfViewerApplication
import com.firsttimeinforever.intellij.pdf.viewer.mpi.model.PageSpreadState
import com.firsttimeinforever.intellij.pdf.viewer.mpi.model.ScrollDirection
import com.firsttimeinforever.intellij.pdf.viewer.mpi.model.ZoomMode
import com.firsttimeinforever.intellij.pdf.viewer.mpi.model.ZoomState

class ViewerAdapter(val viewerApp: PdfViewerApplication) {
  fun addEventListener(event: String, listener: (dynamic) -> Unit) {
    viewerApp.eventBus.on(event, listener)
  }

  val pagesCount: Int
    get() = viewerApp.pdfViewer.pagesCount

  var currentPageNumber: Int
    get() = viewerApp.pdfViewer.currentPageNumber
    set(value) {
      require(value in 1..pagesCount)
      viewerApp.pdfViewer.currentPageNumber = value
    }

  var pageSpreadState: PageSpreadState
    get() = PageSpreadState.values()[viewerApp.pdfViewer.spreadMode]
    set(value) {
      viewerApp.pdfViewer.spreadMode = value.ordinal
    }

  val zoomState: ZoomState
    get() = ZoomState(
      mode = when (viewerApp.pdfViewer.currentScaleValue) {
        "auto" -> ZoomMode.AUTO
        "page-fit" -> ZoomMode.PAGE_FIT
        "page-width" -> ZoomMode.PAGE_WIDTH
        "page-height" -> ZoomMode.PAGE_HEIGHT
        "custom" -> ZoomMode.CUSTOM
        else -> ZoomMode.CUSTOM
      },
      value = viewerApp.pdfViewer.currentScale.toDouble() * 100,
      leftOffset = viewerApp.pdfViewer._location.left,
      topOffset = viewerApp.pdfViewer._location.top
    )

  // Basically direct implementations from the old code

  fun increaseScale() {
    viewerApp.asDynamic().toolbar.items.zoomIn.click()
  }

  fun decreaseScale() {
    viewerApp.asDynamic().toolbar.items.zoomOut.click()
  }

  fun rotateClockwise() {
    viewerApp.asDynamic().appConfig.secondaryToolbar.pageRotateCwButton.click()
  }

  fun rotateCounterClockwise() {
    viewerApp.asDynamic().appConfig.secondaryToolbar.pageRotateCcwButton.click()
  }

  fun findNext(text: String) {
    val findBar = viewerApp.asDynamic().appConfig.findBar
    findBar.findField.value = text
    findBar.findNextButton.click()
  }

  fun findPrevious(text: String) {
    val findBar = viewerApp.asDynamic().appConfig.findBar
    findBar.findField.value = text
    findBar.findPreviousButton.click()
  }

  val currentScrollDirection: ScrollDirection
    get() = ScrollDirection.values()[viewerApp.pdfViewer.scrollMode]

  fun setVerticalScroll() {
    val toolbar = viewerApp.asDynamic().appConfig.secondaryToolbar
    toolbar.scrollVerticalButton.click()
  }

  fun setHorizontalScroll() {
    val toolbar = viewerApp.asDynamic().appConfig.secondaryToolbar
    toolbar.scrollHorizontalButton.click()
  }
}