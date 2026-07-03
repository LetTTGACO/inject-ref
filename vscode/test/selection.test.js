const test = require('node:test');
const assert = require('node:assert/strict');

const { shouldRejectCopyRequest } = require('../out/selection');

test('rejects explorer multi-select', () => {
  assert.equal(shouldRejectCopyRequest({ selectedCount: 2, isDirectory: false }), true);
});

test('rejects directories', () => {
  assert.equal(shouldRejectCopyRequest({ selectedCount: 1, isDirectory: true }), true);
});

test('allows a single file', () => {
  assert.equal(shouldRejectCopyRequest({ selectedCount: 1, isDirectory: false }), false);
});
